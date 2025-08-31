package com.abdulbasit

import java.time.LocalDate

/**
 * Transaction type
 *
 * @constructor Create empty Transaction type
 */

sealed class TransactionType {
    object Borrow : TransactionType()
    object Return : TransactionType()
    data class Renew(val newDueDate: LocalDate) : TransactionType()
}

/**
 * Transaction
 *
 * @property memberId
 * @property itemId
 * @property type
 * @property date
 * @property dueDate
 * @constructor Create empty Transaction
 */
data class Transaction(
    val memberId: String,
    val itemId: String,
    val type: TransactionType,
    val date: LocalDate = LocalDate.now(),
    val dueDate: LocalDate? = null
)

/**
 * Search result
 *
 * @property items
 * @property totalFound
 * @property searchCriteria
 * @constructor Create empty Search result
 */
data class SearchResult(
    val items: List<LibraryItem>,
    val totalFound: Int,
    val searchCriteria: String
)

/**
 * Library utils
 *
 * @constructor Create empty Library utils
 */
class LibraryUtils {
    companion object {
        const val MAX_BORROW_LIMIT = 5
        const val DEFAULT_BORROW_DAYS = 14L

        fun generateItemId(type: String): String =
            type.uppercase().take(1) + System.currentTimeMillis().toString().takeLast(6)

        fun validateISBN(isbn: String): Boolean =
            isbn.replace("-", "").matches(Regex("\\d{10}|\\d{13}"))
    }
}

/**
 * Library config
 *
 * @constructor Create empty Library config
 */
object LibraryConfig {
    val categories = listOf("Fiction", "Non-Fiction", "Reference", "Periodicals")
    const val maxRenewalTimes = 2
    const val lateFeeCap = 50.0
}