package com.abdulbasit


/**
 * Filter by availability
 *
 * @param available
 * @return
 */

fun List<LibraryItem>.filterByAvailability(available: Boolean): List<LibraryItem> =
    this.filter { it.isAvailable == available }

/**
 * Is valid email
 *
 * @return
 */
fun String.isValidEmail(): Boolean =
    this.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))

/**
 * Get formatted info
 *
 * @return
 */

fun LibraryItem.getFormattedInfo(): String = when (this) {
    is Book -> "[Book] '${this.title}' by ${this.author} | ISBN ${this.isbn} | ${if (isAvailable) "Available" else "Out"}"
    is DVD -> "[DVD] '${this.title}' | ${this.genre} | ${this.duration} min | ${if (isAvailable) "Available" else "Out"}"
    is Magazine -> "[Magazine] '${this.title}' | Issue #${this.issueNumber} by ${this.publisher} | ${if (isAvailable) "Available" else "Out"}"
    else -> "[Item] ${this.title} | ${if (isAvailable) "Available" else "Out"}"
}
