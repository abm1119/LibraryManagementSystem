# Library Management System

A comprehensive console-based Library Management System built with Kotlin, demonstrating advanced programming concepts including Object-Oriented Programming, Functional Programming, Collections, Recursion, and modern Kotlin features.

## 🏗️ Architecture Overview

This project demonstrates the following Kotlin concepts:

### 1. Object-Oriented Programming 
- **Abstract Classes & Inheritance**: `LibraryItem` as base class with `Book`, `DVD`, `Magazine` implementations
- **Encapsulation**: Private fields with controlled access through getters/setters
- **Polymorphism**: Different implementations of `calculateLateFee()` and `displayInfo()`

### 2. Collections & HashMaps 
- **Items by ID**: `HashMap<String, LibraryItem>` for fast item lookup
- **Items by Category**: `HashMap<String, MutableList<LibraryItem>>` for categorization
- **Members**: `HashMap<String, Member>` for member management
- **Borrowed Items**: `HashMap<String, MutableList<String>>` for tracking loans

### 3. Functional Programming 
- **Higher-Order Functions**: Methods accepting lambda parameters
- **Extension Functions**: Custom functionality for existing classes
- **Filter/Map/Reduce**: Comprehensive use of functional operations
- **Immutable Data Structures**: Data classes and proper functional style

### 4. Recursion 
- **Compound Interest Calculation**: Recursive late fee calculation
- **Category Hierarchy**: Recursive subcategory traversal
- **Binary Search**: Recursive search implementation

### 5. Advanced Kotlin Features 
- **Data Classes**: `Transaction`, `SearchResult`
- **Sealed Classes**: `TransactionType` with exhaustive when expressions
- **Companion Objects**: Static-like functionality in `LibraryUtils`
- **Object Declarations**: Singleton configuration in `LibraryConfig`

## 🚀 How to Run

### Prerequisites
- JDK 11 or higher
- IntelliJ IDEA (recommended) or any Kotlin-compatible IDE

### Using IntelliJ IDEA
1. Clone/download the project
2. Open in IntelliJ IDEA
3. Wait for Gradle sync to complete
4. Right-click on `Main.kt` → "Run MainKt"

### Using Command Line
```bash
# Build the project
./gradlew build

# Run the application
./gradlew run
```

## 📋 Features

### Core Functionality
- ✅ Add library items (Books, DVDs, Magazines)
- ✅ Register library members
- ✅ Borrow and return items
- ✅ Automatic due date tracking
- ✅ Late fee calculation with compound interest
- ✅ Item renewal system
- ✅ Advanced search functionality
- ✅ Comprehensive reporting

### Advanced Features
- 🔍 **Multi-criteria Search**: Title, author, genre-based searching
- 📊 **Statistics Generation**: Usage patterns, popular items, availability metrics
- 💰 **Dynamic Fee Processing**: Recursive compound interest calculations
- 🔄 **Transaction Logging**: Complete audit trail of all operations
- 📂 **Category Management**: Hierarchical organization system

## 🎮 Sample Usage

The application starts with optional sample data:
- **Books**: "The Kotlin Guide", "Effective Kotlin", "Clean Code"
- **DVDs**: "Kotlin Tutorial", "Advanced Programming", "The Matrix"
- **Magazines**: "Tech Monthly", "Science Today"
- **Members**: Alice Johnson, Bob Lee, Charlie Brown

### Menu Navigation
```
1) Add new library item    - Add books, DVDs, or magazines
2) Register new member     - Create member accounts
3) Borrow item            - Check out items to members
4) Return item            - Return items and calculate fees
5) Search items           - Multiple search options
6) View borrowing history - Member activity tracking
7) Generate reports       - Statistics and analytics
8) Process late fees      - Handle overdue items
9) Renew item            - Extend borrowing period
0) Exit                  - Close application
```

## 🧪 Key Implementations

### Functional Programming Example
```kotlin
// Filter available books by author
fun findBooksByAuthor(author: String): List<Book> {
    return itemsById.values
        .filterIsInstance<Book>()
        .filter { it.author.contains(author, ignoreCase = true) }
        .filter { it.isAvailable }
        .sortedBy { it.title }
}
```

### Recursive Implementation
```kotlin
// Compound late fee calculation
fun calculateCompoundLateFee(baseFee: Double, days: Int): Double {
    if (days <= 0) return baseFee
    return calculateCompoundLateFee(baseFee * 1.05, days - 1)
}
```

### Extension Functions
```kotlin
// Enhanced item formatting
fun LibraryItem.getFormattedInfo(): String = when (this) {
    is Book -> "[Book] '$title' by $author | ISBN $isbn | ${availability}"
    is DVD -> "[DVD] '$title' | $genre | $duration min | ${availability}"
    is Magazine -> "[Magazine] '$title' | Issue #$issueNumber | ${availability}"
    else -> "[Item] $title | ${availability}"
}
```

## 🔧 Design Decisions

### 1. **Separation of Concerns**
- Models in separate files for maintainability
- Business logic concentrated in `Library` class
- UI logic separated in console interface

### 2. **Error Handling**
- Comprehensive input validation
- Graceful failure handling
- User-friendly error messages

### 3. **Performance Optimization**
- HashMap-based lookups for O(1) access
- Efficient filtering and searching
- Minimal object creation

### 4. **Extensibility**
- Easy to add new item types
- Configurable business rules
- Modular component design

## 📈 Testing Examples

The application includes comprehensive examples of all implemented concepts:

1. **OOP Demo**: Creating different item types and seeing polymorphic behavior
2. **Functional Demo**: Using search and filter operations
3. **Recursion Demo**: Calculating compound late fees
4. **Collections Demo**: Managing different data structures efficiently

## 🎯 Learning Outcomes

After exploring this project, you will understand:

- ✅ How to design class hierarchies with inheritance and polymorphism
- ✅ Effective use of Kotlin collections, especially HashMaps
- ✅ Writing functional-style code with higher-order functions
- ✅ Implementing recursive algorithms effectively
- ✅ Using advanced Kotlin features like data classes and sealed classes
- ✅ Building complete, working applications that integrate multiple concepts

## 🤝 Contributing

This is an educational project demonstrating Kotlin concepts. Feel free to:
- Add new features (recommendation system, data persistence)
- Implement additional design patterns
- Add comprehensive unit tests
- Enhance the user interface

## 📝 Assignment Compliance

This implementation fulfills all assignment requirements:
- ✅ All 5 parts implemented (OOP, Collections, Functional, Recursion, Advanced)
- ✅ Console-based interactive application
- ✅ Proper code organization and documentation
- ✅ Sample data and usage examples
- ✅ Error handling and validation

---

**Happy Coding! 🚀**