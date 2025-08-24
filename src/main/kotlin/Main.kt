package com.abdulbasit

import kotlin.system.exitProcess


private fun prompt(msg: String): String {
    print(msg)
    return readln().trim()
}

private fun printMenu() {
    println(
        """
        ---------------- Library Console ----------------
        1) Add new library item
        2) Register new member
        3) Borrow item
        4) Return item
        5) Search items
        6) View member borrowing history
        7) Generate library reports
        8) Calculate and process late fees
        9) Renew item (bonus)
        0) Exit
        -------------------------------------------------
        """.trimIndent()
    )
}

private fun addSampleData(library: Library) {
    // Sample items
    val b1 = Book("B001", "The Kotlin Guide", "Kashif", "9781234567890", 300)
    val b2 = Book("B002", "Effective Kotlin", "AbdulBasit", "9788328343787", 360)
    val d1 = DVD("D001", "Kotlin Tutorial", "Ahmed Ali", 120, "Educational")
    val m1 = Magazine("M001", "Tech Monthly", 42, "TechPress")

    library.addItem(b1, "Non-Fiction")
    library.addItem(b2, "Non-Fiction")
    library.addItem(d1, "Reference")
    library.addItem(m1, "Periodicals")


    // Members
    val alice = Member("M001", "Alice Johnson", "alice@email.com")
    val bob = Member("M002", "Bob Lee", "bob.lee@email.com")
    library.registerMember(alice)
    library.registerMember(bob)
}

/**
  Main Function --
 */
fun main() {
    val library = Library()
    addSampleData(library)

    while (true) {
        printMenu()
        when (prompt("Choose: ")) {
            "1" -> {
                println("Add Item: 1) Book 2) DVD 3) Magazine")
                when (prompt("Type: ")) {
                    "1" -> {
                        val id = LibraryUtils.generateItemId("B")
                        val title = prompt("Title: ")
                        val author = prompt("Author: ")
                        val isbn = prompt("ISBN (10 or 13 digits, dashes ok): ")
                        if (!LibraryUtils.validateISBN(isbn)) {
                            println("Invalid ISBN.")
                            continue
                        }
                        val pages = prompt("Pages: ").toIntOrNull() ?: run {
                            println("Invalid pages.")
                            continue
                        }
                        val category = prompt("Category (${LibraryConfig.categories}): ")
                        library.addItem(Book(id, title, author, isbn, pages), category)
                        println("Book added with ID $id")
                    }
                    "2" -> {
                        val id = LibraryUtils.generateItemId("D")
                        val title = prompt("Title: ")
                        val director = prompt("Director: ")
                        val duration = prompt("Duration (minutes): ").toIntOrNull() ?: run {
                            println("Invalid duration.")
                            continue
                        }
                        val genre = prompt("Genre: ")
                        val category = prompt("Category (${LibraryConfig.categories}): ")
                        library.addItem(DVD(id, title, director, duration, genre), category)
                        println("DVD added with ID $id")
                    }
                    "3" -> {
                        val id = LibraryUtils.generateItemId("M")
                        val title = prompt("Title: ")
                        val issue = prompt("Issue number: ").toIntOrNull() ?: run {
                            println("Invalid issue.")
                            continue
                        }
                        val publisher = prompt("Publisher: ")
                        val category = prompt("Category (${LibraryConfig.categories}): ")
                        library.addItem(Magazine(id, title, issue, publisher), category)
                        println("Magazine added with ID $id")
                    }
                    else -> println("Unknown type.")
                }
            }
            "2" -> {
                val id = prompt("Member ID: ")
                val name = prompt("Name: ")
                val email = prompt("Email: ")
                if (!email.isValidEmail()) {
                    println("Invalid email.")
                    continue
                }
                library.registerMember(Member(id, name, email))
                println("Member registered.")
            }
            "3" -> {
                val memberId = prompt("Member ID: ")
                val itemId = prompt("Item ID: ")
                val ok = library.borrowItem(memberId, itemId)
                println(if (ok) "Borrowed." else "Borrow failed.")
            }
            "4" -> {
                val memberId = prompt("Member ID: ")
                val itemId = prompt("Item ID: ")
                val (ok, fee) = library.returnItem(memberId, itemId)
                if (ok) {
                    println("Returned. Late fee: $${"%.2f".format(fee)}")
                } else println("Return failed.")
            }
            "5" -> {
                println("Search: 1) By title  2) Books by author  3) Generic filter")
                when (prompt("Type: ")) {
                    "1" -> {
                        val q = prompt("Title contains: ")
                        val res = library.searchByTitle(q)
                        println("Found ${res.totalFound} item(s) for ${res.searchCriteria}")
                        res.items.forEach { println(it.getFormattedInfo()) }
                    }
                    "2" -> {
                        val author = prompt("Author: ")
                        val items = library.findBooksByAuthor(author).filter { it.isAvailable }.map { it.getFormattedInfo() }
                        if (items.isEmpty()) println("No available books by $author")
                        else items.forEach(::println)
                    }
                    "3" -> {
                        println("Generic: filter DVDs by genre")
                        val genre = prompt("Genre equals: ")
                        val items = library.findItemsBy(DVD::class.java) { it.genre.equals(genre, true) }
                        if (items.isEmpty()) println("No DVDs in genre '$genre'")
                        else items.forEach { println(it.getFormattedInfo()) }
                    }
                    else -> println("Unknown search type.")
                }
            }
            "6" -> {
                val memberId = prompt("Member ID: ")
                val ids = library.memberBorrowedIds(memberId)
                if (ids.isEmpty()) println("No items currently borrowed.")
                else {
                    println("Borrowed item IDs: ${ids.joinToString()}")
                }
            }
            "7" -> {
                val stats = library.getLibraryStatistics()
                println("Total by type: " + stats["totalByType"])
                println("Average pages for books: " + stats["averagePagesForBooks"])
                println("Most popular DVD genre: " + stats["mostPopularGenreForDVDs"])
                println("Availability %: ${"%.2f".format(stats["percentageAvailable"] as Double)}")
            }
            "8" -> {
                println("Processing overdue items:")
                library.processOverdueItems { item, member, daysLate ->
                    val base = item.calculateLateFee(daysLate)
                    val compounded = calculateCompoundLateFee(base, daysLate)
                    val capped = compounded.coerceAtMost(LibraryConfig.lateFeeCap)
                    println(" - ${item.title} (held by ${member.getName()}) is $daysLate day(s) late. Base: $${"%.2f".format(base)}, Compounded: $${"%.2f".format(compounded)}, Charged: $${"%.2f".format(capped)}")
                }
            }
            "9" -> {
                val memberId = prompt("Member ID: ")
                val itemId = prompt("Item ID: ")
                val ok = library.renewItem(memberId, itemId)
                println(if (ok) "Renewed." else "Renew failed (limit reached or not owned).")
            }
            "0" -> {
                println("Goodbye!")
                exitProcess(0)
            }
            else -> println("Unknown choice.")
        }
        println()
    }
}
