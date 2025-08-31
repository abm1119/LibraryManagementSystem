package com.abdulbasit

/**
 * Calculate compound late fee
 *
 * @param baseFee
 * @param days
 * @return
 */

fun calculateCompoundLateFee(baseFee: Double, days: Int): Double =
    if (days <= 0) baseFee else calculateCompoundLateFee(baseFee * 1.05, days - 1)

/**
 * Find all subcategories
 *
 * @param category
 * @param hierarchy
 * @return
 */
fun findAllSubcategories(category: String, hierarchy: Map<String, List<String>>): List<String> =
    hierarchy[category].orEmpty().flatMap { listOf(it) + findAllSubcategories(it, hierarchy) }

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
fun <T : Comparable<T>> recursiveBinarySearch(list: List<T>, target: T, low: Int = 0, high: Int = list.size - 1): Int =
    if (low > high) -1 else {
        val mid = (low + high) / 2
        when {
            list[mid] == target -> mid
            list[mid] > target -> recursiveBinarySearch(list, target, low, mid - 1)
            else -> recursiveBinarySearch(list, target, mid + 1, high)
        }
    }