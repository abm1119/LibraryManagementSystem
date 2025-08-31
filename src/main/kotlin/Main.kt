package com.abdulbasit

import kotlin.system.exitProcess

/**
 * Lib
 */
private val lib = Library()

/**
 * Main
 *
 */

fun main() {
    addSampleData()
    menu()
}

/**
 * Menu
 *
 */
private fun menu() {
    while (true) {
        println(
            """
          --======= Library Menu ======---
            1) Add new library item
            2) Register new member
            3) Borrow item
            4) Return item
            5) Search items
            6) View member borrowing history
            7) Generate library reports
            8) Calculate and process late fees
            9) Renew item
            0) Exit
            ______________________________________
            Choice here : 
            """.trimIndent()
        )
        when (readln().trim()) {
            "1" -> addItemCli()
            "2" -> registerCli()
            "3" -> borrowCli()
            "4" -> returnCli()
            "5" -> searchCli()
            "6" -> historyCli()
            "7" -> reportCli()
            "8" -> overdueCli()
            "9" -> renewCli()
            "0" -> exitProcess()
            else -> println("Unknown choice")
        }
    }
}


/**
 * Add sample data
 * CLI helpers
 */

private fun addSampleData() {
    lib.addItem(
        Book("B1", "The Kotlin Guide", "Moazzam", "9781234567890", 300),
        "Non-Fiction"
    )
    lib.addItem(
        Book("B2", "Effective Kotlin", "Mark Zee ", "9788328343787", 360),
        "Non-Fiction"
    )
    lib.addItem(
        DVD("D1", "Kotlin Tutorial", "Alexander", 120, "Educational"),
        "Reference"
    )
    lib.addItem(
        Magazine("M1", "Tech Monthly", 42, "TechPress"),
        "Periodicals"
    )
    lib.registerMember(Member("M1", "joe Root", "jroot@email.com"))
    lib.registerMember(Member("M2", "Bruce Lee", "bruce.lee@email.com"))
}

private fun prompt(msg: String) = print(msg).let { readln().trim() }

/**
 * Add item cli
 *
 */
private fun addItemCli() {
    println("Type: 1-Book 2-DVD 3-Magazine")
    when (readln().trim()) {
        "1" -> {
            val id = LibraryUtils.generateItemId("B")
            val title = prompt("Title: ")
            val author = prompt("Author: ")
            val isbn = prompt("ISBN (10/13): ")
            if (!LibraryUtils.validateISBN(isbn)) {
                println("Invalid ISBN"); return
            }
            val pages = prompt("Pages: ").toIntOrNull() ?: 0
            lib.addItem(Book(id, title, author, isbn, pages), prompt("Category (${LibraryConfig.categories}): "))
            println("Book added with ID $id")
        }
        "2" -> {
            val id = LibraryUtils.generateItemId("D")
            val title = prompt("Title: ")
            val director = prompt("Director: ")
            val duration = prompt("Duration (min): ").toIntOrNull() ?: 0
            val genre = prompt("Genre: ")
            lib.addItem(DVD(id, title, director, duration, genre), prompt("Category: "))
            println("DVD added with ID $id")
        }
        "3" -> {
            val id = LibraryUtils.generateItemId("M")
            val title = prompt("Title: ")
            val issue = prompt("Issue #: ").toIntOrNull() ?: 0
            val publisher = prompt("Publisher: ")
            lib.addItem(Magazine(id, title, issue, publisher), prompt("Category: "))
            println("Magazine added with ID $id")
        }
        else -> println("Unknown type")
    }
}

private fun registerCli() {
    val id = prompt("Member ID (leave blank to auto-generate): ")
        .ifBlank { LibraryUtils.generateItemId("M") }
    val name = prompt("Name: ")
    val email = prompt("Email: ")
    if (!email.isValidEmail()) {
        println("Invalid email"); return
    }
    lib.registerMember(Member(id, name, email))
    println("Member registered")
}

private fun borrowCli() {
    val ok = lib.borrowItem(prompt("Member ID: "), prompt("Item ID: "))
    println(if (ok) "Borrowed" else "Borrow failed")
}

private fun returnCli() {
    val (ok, fee) = lib.returnItem(prompt("Member ID: "), prompt("Item ID: "))
    if (ok) println("Returned – late fee \$${"%.2f".format(fee)}") else println("Return failed")
}

private fun searchCli() {
    println("Search: 1-By title  2-Books by author  3-DVDs by genre")
    when (readln().trim()) {
        "1" -> {
            val q = prompt("Title contains: ")
            val res = lib.searchByTitle(q)
            println("Found ${res.totalFound} item(s)")
            res.items.forEach { println(it.getFormattedInfo()) }
        }
        "2" -> {
            val author = prompt("Author: ")
            lib.findBooksByAuthor(author)
                .filter { it.isAvailable }
                .forEach { println(it.getFormattedInfo()) }
        }
        "3" -> {
            val genre = prompt("Genre: ")
            lib.findItemsBy(DVD::class.java) { it.genre.equals(genre, ignoreCase = true) }
                .forEach { println(it.getFormattedInfo()) }
        }
    }
}

/**
 * History cli
 *
 */
private fun historyCli() {
    val ids = lib.memberBorrowedIds(prompt("Member ID: "))
    if (ids.isEmpty()) println("No items currently borrowed")
    else println("Borrowed IDs: ${ids.joinToString()}")
}

private fun reportCli() {
    println(lib.getLibraryStatistics())
}

private fun overdueCli() {
    println("Processing overdue items:")
    lib.processOverdueItems { item, member, days ->
        val base = item.calculateLateFee(days)
        val compound = calculateCompoundLateFee(base, days).coerceAtMost(LibraryConfig.lateFeeCap)
        println(
            " - ${item.title} (${member.getName()}) is $days day(s) late. " +
                    "Base: \$${"%.2f".format(base)}, Charged: \$${"%.2f".format(compound)}"
        )
    }
}

private fun renewCli() {
    val ok = lib.renewItem(prompt("Member ID: "), prompt("Item ID: "))
    println(if (ok) "Renewed" else "Renew failed")
}

/**
 * Exit process
 *
 */
private fun exitProcess(){
    // user exit from here
    println("Good Bye!")
    exitProcess(0)

}