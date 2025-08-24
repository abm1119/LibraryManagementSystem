package com.abdulbasit

/**
 * Abstract base class for all items in the library.
 *
 * @property id Unique identifier of the item
 * @property title Title of the library item
 * @property isAvailable Availability status
 */

abstract class LibraryItem(
    val id: String,
    val title: String,
    var isAvailable: Boolean = true
) {
    abstract fun getItemType(): String
    /**
     * Calculates the late fee based on overdue days.
     * @param daysLate Number of overdue days
     * @return Calculated late fee
     */
    abstract fun calculateLateFee(daysLate: Int): Double
    /** Returns a formatted string with item details. */
    open fun displayInfo(): String =
        "ID: $id, Title: $title, Available: $isAvailable"
}
/**
 * Represents a Book in the library.
 */
class Book(
    id: String,
    title: String,
    val author: String,
    val isbn: String,
    val pages: Int
) : LibraryItem(id, title) {
    override fun getItemType() = "Book"
    /**
     * Calculates the late fee based on overdue days.
     * @param daysLate Number of overdue days
     * @return Calculated late fee
     */
    override fun calculateLateFee(daysLate: Int): Double = 0.50 * daysLate.coerceAtLeast(0)

    override fun displayInfo(): String =
        super.displayInfo() + ", Author: $author, ISBN: $isbn, Pages: $pages"
}

/**
 * Represents a DVD in the library.
 */

class DVD(
    id: String,
    title: String,
    val director: String,
    val duration: Int, // minutes
    val genre: String
) : LibraryItem(id, title) {
    override fun getItemType() = "DVD"
    /**
     * Calculates the late fee based on overdue days.
     * @param daysLate Number of overdue days
     * @return Calculated late fee
     */
    override fun calculateLateFee(daysLate: Int): Double = 1.00 * daysLate.coerceAtLeast(0)
    override fun displayInfo(): String =
        super.displayInfo() + ", Director: $director, Duration: ${duration}m, Genre: $genre"
}

/**
 * Represents a Magazine in the library.
 */
class Magazine(
    id: String,
    title: String,
    val issueNumber: Int,
    val publisher: String
) : LibraryItem(id, title) {
    override fun getItemType() = "Magazine"
    /**
     * Calculates the late fee based on overdue days.
     * @param daysLate Number of overdue days
     * @return Calculated late fee
     */
    override fun calculateLateFee(daysLate: Int): Double = 0.25 * daysLate.coerceAtLeast(0)

    override fun displayInfo(): String =
        super.displayInfo() + ", Issue: $issueNumber, Publisher: $publisher"
}
