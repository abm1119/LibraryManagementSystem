package com.abdulbasit

/**
 * Filter by availability
 *
 * @param available
 * @return
 */

fun List<LibraryItem>.filterByAvailability(available: Boolean): List<LibraryItem> =
    filter { it.isAvailable == available }

/**
 * Is valid email
 *
 * @return
 */
fun String.isValidEmail(): Boolean =
    matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))

/**
 * Get formatted info
 *
 * @return
 */

fun LibraryItem.getFormattedInfo(): String = when (this) {
    is Book -> "[Book] '$title' by $author | ISBN $isbn | ${if (isAvailable) "Available" else "On loan"}"
    is DVD -> "[DVD] '$title' | $genre | ${duration}min | ${if (isAvailable) "Available" else "On loan"}"
    is Magazine -> "[Magazine] '$title' | Issue #$issueNumber | $publisher | ${if (isAvailable) "Available" else "On loan"}"
    else -> "[Item] $title | ${if (isAvailable) "Available" else "On loan"}"
}