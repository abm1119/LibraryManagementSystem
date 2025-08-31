package com.abdulbasit

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Library
 *
 * @constructor Create empty Library
 */


class Library {
    private val itemsById = HashMap<String, LibraryItem>()
    private val itemsByCategory = HashMap<String, MutableList<LibraryItem>>()
    private val members = HashMap<String, Member>()
    private val borrowedByMember = HashMap<String, MutableList<String>>() // memberId -> list of itemIds
    private val dueDates = HashMap<String, LocalDate>()                  // itemId -> due date
    private val renewCount = HashMap<String, Int>()

    /* Inventory */
    fun addItem(item: LibraryItem, category: String) {
        itemsById[item.id] = item
        itemsByCategory.getOrPut(category) { mutableListOf() } += item
    }

    fun registerMember(member: Member) { members[member.getMemberId()] = member }

    /**
     * Borrow item
     *
     * @param memberId
     * @param itemId
     * @return
     */

    fun borrowItem(memberId: String, itemId: String): Boolean {
        val member = members[memberId] ?: return false
        val item = itemsById[itemId] ?: return false
        if (!item.isAvailable) return false
        if ((borrowedByMember[memberId]?.size ?: 0) >= LibraryUtils.MAX_BORROW_LIMIT) return false

        item.isAvailable = false
        borrowedByMember.getOrPut(memberId) { mutableListOf() } += itemId
        member._borrow(item)
        dueDates[itemId] = LocalDate.now().plusDays(LibraryUtils.DEFAULT_BORROW_DAYS.toLong())
        renewCount[itemId] = 0
        return true
    }

    /**
     * Return item
     *
     * @param memberId
     * @param itemId
     * @return
     */
    fun returnItem(memberId: String, itemId: String): Pair<Boolean, Double> {
        val list = borrowedByMember[memberId] ?: return false to 0.0
        if (!list.remove(itemId)) return false to 0.0
        val item = itemsById[itemId] ?: return false to 0.0
        item.isAvailable = true
        members[memberId]?._return(item)
        val days = ChronoUnit.DAYS.between(dueDates[itemId]!!, LocalDate.now()).toInt()
            .coerceAtLeast(0)
        val fee = item.calculateLateFee(days).coerceAtMost(LibraryConfig.lateFeeCap)
        dueDates -= itemId
        renewCount -= itemId
        return true to fee
    }

    fun renewItem(memberId: String, itemId: String): Boolean {
        if (itemId !in (borrowedByMember[memberId] ?: emptyList())) return false
        val n = renewCount.getOrDefault(itemId, 0)
        if (n >= LibraryConfig.maxRenewalTimes) return false
        dueDates[itemId] = dueDates[itemId]!!.plusDays(LibraryUtils.DEFAULT_BORROW_DAYS.toLong())
        renewCount[itemId] = n + 1
        return true
    }

    fun findBooksByAuthor(author: String): List<Book> =
        itemsById.values.filterIsInstance<Book>().filter { it.author.equals(author, ignoreCase = true) }

    fun <T : LibraryItem> findItemsBy(type: Class<T>, predicate: (T) -> Boolean): List<T> =
        itemsById.values.filter { type.isInstance(it) }
            .map { type.cast(it)!! }
            .filter(predicate)

    fun searchByTitle(query: String): SearchResult {
        val list = itemsById.values.filter { it.title.contains(query, ignoreCase = true) }
        return SearchResult(list, list.size, "title contains '$query'")
    }

    fun getLibraryStatistics(): Map<String, Any> {
        val books = itemsById.values.filterIsInstance<Book>()
        val dvds = itemsById.values.filterIsInstance<DVD>()
        val totalByType = itemsById.values.groupBy { it.getItemType() }.mapValues { it.value.size }
        val avgPages = books.map { it.pages }.average()
        val availablePct = itemsById.values.count { it.isAvailable } * 100.0 / itemsById.size
        return mapOf(
            "Total by type" to totalByType,
            "Average pages (books)" to avgPages,
            "Available %" to "%.2f".format(availablePct)
        )
    }

    fun processOverdueItems(action: (LibraryItem, Member, Int) -> Unit) {
        dueDates.forEach { (itemId, due) ->
            val days = ChronoUnit.DAYS.between(due, LocalDate.now()).toInt()
            if (days > 0) {
                val item = itemsById[itemId] ?: return@forEach
                val memberId = borrowedByMember.entries.firstOrNull { itemId in it.value }?.key ?: return@forEach
                val member = members[memberId] ?: return@forEach
                action(item, member, days)
            }
        }
    }

    /**
     * Member borrowed ids
     *
     * @param memberId
     * @return
     */
    /* Helpers */
    fun memberBorrowedIds(memberId: String): List<String> =
        borrowedByMember[memberId].orEmpty()
}
