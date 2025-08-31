package com.abdulbasit

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
    val date: LocalDate,
    val dueDate: LocalDate? = null
)

/**
 * Transaction type
 *
 * @constructor Create empty Transaction type
 */
sealed class TransactionType {
    data object Borrow : TransactionType()
    data object Return : TransactionType()
    data class Renew(val newDueDate: LocalDate) : TransactionType()
}

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
        const val DEFAULT_BORROW_DAYS = 14

        fun generateItemId(type: String): String {
            val ts = System.currentTimeMillis()
            val prefix = type.uppercase(Locale.getDefault()).take(1)
            return "${prefix}${ts.toString().takeLast(7)}"
        }

        // Simple ISBN-10/13 check (format only)
        fun validateISBN(isbn: String): Boolean {
            val cleaned = isbn.replace("-", "").trim()

            return cleaned.matches(Regex("^(?:\\d{10}|\\d{13})$"))
        }
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
    val dateFmt: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
}