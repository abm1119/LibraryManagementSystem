package com.abdulbasit

import java.time.LocalDate

/**
 * Represents the main Library class where members and items are managed.
 */
class Library {

    // ---------------- Collections & HashMaps ----------------

    private val itemsById = HashMap<String, LibraryItem>()
    private val itemsByCategory = HashMap<String, MutableList<LibraryItem>>()
    private val members = HashMap<String, Member>()
    private val borrowedItems = HashMap<String, MutableList<String>>() // memberId -> list of itemIds

    // Additional tracking to support due dates and overdue processing
    private val transactions = mutableListOf<Transaction>()
    private val loanDueDates = HashMap<String, LocalDate>() // itemId -> due date
    private val renewalsCount = HashMap<String, Int>() // itemId -> times renewed

    // ------------- Inventory & Members -------------

    fun addItem(item: LibraryItem, category: String) {
        itemsById[item.id] = item
        val list = itemsByCategory.getOrPut(category) { mutableListOf() }
        list += item
    }

    fun registerMember(member: Member) {
        members[member.getMemberId()] = member
    }

    fun getMember(memberId: String): Member? = members[memberId]
    fun getItem(itemId: String): LibraryItem? = itemsById[itemId]

    // ------------- Borrowing & Returning -------------

    fun borrowItem(memberId: String, itemId: String): Boolean {
        val member = members[memberId] ?: return false
        val item = itemsById[itemId] ?: return false

        if (!item.isAvailable) return false
        val memberBorrowedCount = borrowedItems[memberId]?.size ?: 0
        if (memberBorrowedCount >= LibraryUtils.MAX_BORROW_LIMIT) return false

        item.isAvailable = false
        val list = borrowedItems.getOrPut(memberId) { mutableListOf() }
        list += itemId
        member._borrow(item)

        val due = LocalDate.now().plusDays(LibraryUtils.DEFAULT_BORROW_DAYS.toLong())
        loanDueDates[itemId] = due
        transactions += Transaction(memberId, itemId, TransactionType.Borrow, LocalDate.now(), due)
        return true
    }

    fun returnItem(memberId: String, itemId: String): Pair<Boolean, Double> {
        val member = members[memberId] ?: return false to 0.0
        val item = itemsById[itemId] ?: return false to 0.0

        val ids = borrowedItems[memberId] ?: return false to 0.0
        if (!ids.remove(itemId)) return false to 0.0

        item.isAvailable = true
        member._return(item)

        val due = loanDueDates.remove(itemId)
        val daysLate = if (due != null) {
            val days = java.time.temporal.ChronoUnit.DAYS.between(due, LocalDate.now()).toInt()
            days.coerceAtLeast(0)
        } else 0

        transactions += Transaction(memberId, itemId, TransactionType.Return, LocalDate.now(), null)
        val fee = item.calculateLateFee(daysLate).coerceAtMost(LibraryConfig.lateFeeCap)
        return true to fee
    }

    fun renewItem(memberId: String, itemId: String): Boolean {
        val member = members[memberId] ?: return false
        val item = itemsById[itemId] ?: return false
        if (item.isAvailable) return false // can't renew if not borrowed

        val owned = borrowedItems[memberId]?.contains(itemId) ?: false
        if (!owned) return false

        val count = renewalsCount.getOrDefault(itemId, 0)
        if (count >= LibraryConfig.maxRenewalTimes) return false

        val newDue = (loanDueDates[itemId] ?: LocalDate.now()).plusDays(LibraryUtils.DEFAULT_BORROW_DAYS.toLong())
        loanDueDates[itemId] = newDue
        renewalsCount[itemId] = count + 1
        transactions += Transaction(memberId, itemId, TransactionType.Renew(newDue), LocalDate.now(), newDue)
        return true
    }

    // ------------- Searching -------------

    fun searchByTitle(query: String): SearchResult {
        val q = query.trim().lowercase()
        val items = itemsById.values.filter { it.title.lowercase().contains(q) }
        return SearchResult(items, items.size, "title contains '$query'")
    }

    fun findBooksByAuthor(author: String): List<Book> =
        itemsById.values
            .filterIsInstance<Book>()
            .filter { it.author.equals(author, ignoreCase = true) }

    fun <T : LibraryItem> findItemsBy(
        type: Class<T>,
        predicate: (T) -> Boolean
    ): List<T> {
        return itemsById.values
            .filter { type.isInstance(it) }
            .map { type.cast(it)!! }
            .filter(predicate)
    }

    fun getLibraryStatistics(): Map<String, Any> {
        val items = itemsById.values.toList()
        val totalByType = items.groupBy { it.getItemType() }.mapValues { (_, v) -> v.size }
        val avgPages = items.filterIsInstance<Book>().map { it.pages }.let {
            if (it.isEmpty()) 0.0 else it.average()
        }

        val dvdBorrowsByGenre = transactions
            .filter { it.type is TransactionType.Borrow }
            .mapNotNull { tr -> (itemsById[tr.itemId] as? DVD)?.genre }
            .groupBy { it }
            .mapValues { it.value.size }

        val mostPopularGenre = dvdBorrowsByGenre.maxByOrNull { it.value }?.key

        val availablePct = if (items.isEmpty()) 0.0
        else items.count { it.isAvailable }.toDouble() / items.size * 100.0

        return mapOf(
            "totalByType" to totalByType,
            "averagePagesForBooks" to avgPages,
            "mostPopularGenreForDVDs" to (mostPopularGenre ?: "N/A"),
            "percentageAvailable" to availablePct
        )
    }

    fun processOverdueItems(action: (LibraryItem, Member, Int) -> Unit) {
        val today = LocalDate.now()
        loanDueDates.forEach { (itemId, due) ->
            if (due.isBefore(today)) {
                val item = itemsById[itemId] ?: return@forEach
                // Find the member who holds this item
                val holderId = borrowedItems.entries.firstOrNull { it.value.contains(itemId) }?.key ?: return@forEach
                val member = members[holderId] ?: return@forEach
                val daysLate = java.time.temporal.ChronoUnit.DAYS.between(due, today).toInt().coerceAtLeast(0)
                action(item, member, daysLate)
            }
        }
    }

    // Helpers
    fun itemsInCategory(category: String): List<LibraryItem> =
        itemsByCategory[category].orEmpty()

    fun memberBorrowedIds(memberId: String): List<String> =
        borrowedItems[memberId].orEmpty().toList()

    fun allTransactions(): List<Transaction> = transactions.toList()

}