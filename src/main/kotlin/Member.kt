package com.abdulbasit

/**
 * Represents a member of the library.
 */
class Member(private val memberId: String,
             private var name: String,
             private var email: String)
{
    private val borrowedItems = mutableListOf<LibraryItem>()

    fun getMemberId(): String = memberId
    fun getName(): String = name
    fun setName(newName: String) {
        require(newName.isNotBlank()) { "Name cannot be blank." }
        name = newName.trim()
    }

    fun getEmail(): String = email
    fun setEmail(newEmail: String) {
        require(newEmail.isValidEmail()) { "Invalid email address." }
        email = newEmail.trim()
    }

    fun listBorrowed(): List<LibraryItem> = borrowedItems.toList()

    internal fun _borrow(item: LibraryItem) {
        borrowedItems += item
    }

    internal fun _return(item: LibraryItem) {
        borrowedItems.remove(item)
    }


    fun calculateTotalLateFees(daysLateByItemId: Map<String, Int>): Double =
        borrowedItems.sumOf { it.calculateLateFee(daysLateByItemId[it.id] ?: 0) }
}