package com.abdulbasit


abstract class LibraryItem(
    val id: String,
    val title: String,
    var isAvailable: Boolean = true
) {
    abstract fun getItemType(): String
    abstract fun calculateLateFee(daysLate: Int): Double
    open fun displayInfo(): String =
        "ID: $id, Title: $title, Available: $isAvailable"
}

class Book(
    id: String,
    title: String,
    val author: String,
    val isbn: String,
    val pages: Int
) : LibraryItem(id, title) {
    override fun getItemType() = "Book"
    override fun calculateLateFee(daysLate: Int) = 0.50 * daysLate.coerceAtLeast(0)
    override fun displayInfo() =
        super.displayInfo() + ", Author: $author, ISBN: $isbn, Pages: $pages"
}

class DVD(
    id: String,
    title: String,
    val director: String,
    val duration: Int,
    val genre: String
) : LibraryItem(id, title) {
    override fun getItemType() = "DVD"
    override fun calculateLateFee(daysLate: Int) = 1.00 * daysLate.coerceAtLeast(0)
    override fun displayInfo() =
        super.displayInfo() + ", Director: $director, Duration: ${duration}min, Genre: $genre"
}

class Magazine(
    id: String,
    title: String,
    val issueNumber: Int,
    val publisher: String
) : LibraryItem(id, title) {
    override fun getItemType() = "Magazine"
    override fun calculateLateFee(daysLate: Int) = 0.25 * daysLate.coerceAtLeast(0)
    override fun displayInfo() =
        super.displayInfo() + ", Issue: $issueNumber, Publisher: $publisher"
}