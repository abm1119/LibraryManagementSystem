package com.abdulbasit

/**
 * Calculate compound late fee
 *
 * @param baseFee
 * @param days
 * @return
 */

fun calculateCompoundLateFee(baseFee: Double, days: Int): Double {
    if (days <= 0) return baseFee
    // Each day adds 5% compound interest recursively
    return calculateCompoundLateFee(baseFee * 1.05, days - 1)
}

/**
 * Find all subcategories
 *
 * @param category
 * @param categoryHierarchy
 * @return
 */

fun findAllSubcategories(category: String, categoryHierarchy: Map<String, List<String>>): List<String> {
    val direct = categoryHierarchy[category].orEmpty()
    if (direct.isEmpty()) return emptyList()
    // Recursively gather children of each direct child
    val deeper = direct.flatMap { child -> findAllSubcategories(child, categoryHierarchy) }
    return (direct + deeper).distinct()
}

/**
 * Recursive binary search
 *
 * @param T
 * @param list
 * @param target
 * @param low
 * @param high
 * @return
 */

fun <T : Comparable<T>> recursiveBinarySearch(
    list: List<T>,
    target: T,
    low: Int = 0,
    high: Int = list.size - 1
): Int {
    if (low > high) return -1
    val mid = low + (high - low) / 2
    return when {
        list[mid] == target -> mid
        list[mid] > target -> recursiveBinarySearch(list, target, low, mid - 1)
        else -> recursiveBinarySearch(list, target, mid + 1, high)
    }
}