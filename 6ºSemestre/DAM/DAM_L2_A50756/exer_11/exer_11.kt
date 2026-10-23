data class Book(
    val title : String,
    val author : String,
    val year : Int
)

fun interface BookComparator{
    fun compare (b1: Book , b2: Book ) : Int
}

fun sortBooks(books : List<Book>, typeOfSort : BookComparator) : List<Book>{
    return books.sortedWith { a, b -> typeOfSort.compare(a, b) }
}

fun main() {
    val books = listOf(
        Book("1984", "George Orwell", 1949),
        Book("Brave New World", "Aldous Huxley", 1932),
        Book("Fahrenheit 451", "Ray Bradbury", 1953),
        Book("The Hobbit", "J.R.R. Tolkien", 1937)
    )

    // title
    val sortedByTitle = sortBooks(books) { b1, b2 -> b1.title.compareTo(b2.title) }
    println("Sorted by Title: $sortedByTitle")

    // year
    val sortedByYear = sortBooks(books) { b1, b2 -> b1.year - b2.year }
    println("\nSorted by Year: $sortedByYear")

    // author
    val sortedByAuthor = sortBooks(books) { b1, b2 -> b1.author.compareTo(b2.author) }
    println("\nSorted by Author: $sortedByAuthor")
}