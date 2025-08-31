package com.abdulbasit

/**
 * Member
 *
 * @property memberId
 * @property name
 * @property email
 * @constructor Create empty Member
 */

class Member (
    private val memberId: String,
    private var name: String,
    private var email: String
) {
    private val borrowedItems = mutableListOf<LibraryItem>()

    fun getMemberId() = memberId
    fun getName() = name
    fun setName( new: String){
        require(new.isNotBlank()) {"Name cannot blank"}; name = new
    }

    fun getEmail()= email
    fun setEmail(new: String){
        require(new.isValidEmail()) {"Invalid Email"}; email = new }

    fun listBorrowed(): List<LibraryItem> = borrowedItems.toList()
    internal fun _borrow(item: LibraryItem){ borrowedItems += item}
    internal fun _return(item: LibraryItem){ borrowedItems += item}

    fun totalLateFees(daysMap: Map<String, Int>) =
        borrowedItems.sumOf { it.calculateLateFee(daysMap[it.id] ?:0) }
}