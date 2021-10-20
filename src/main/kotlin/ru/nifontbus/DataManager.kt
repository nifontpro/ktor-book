package ru.nifontbus

import org.slf4j.LoggerFactory
import kotlin.reflect.full.declaredMemberProperties

class DataManager {

    private val log = LoggerFactory.getLogger(DataManager::class.java)
    private var books = ArrayList<Book>()

    fun gimmeId(): String = books.size.toString()

    init {
        with(books) {
            add(Book(gimmeId(), "First Biblia", "Alpha", 500f))
            add(Book(gimmeId(), "Новый Завет", "Слово", 330f))
            add(Book(gimmeId(), "Детские сказки", "Крошка", 250f))
            add(Book(gimmeId(), "Братья Карамазовы", "Бастион", 500f))
            add(Book(gimmeId(), "Аскетические опыты", "Alpha", 777f))
            add(Book(gimmeId(), "Служебник", "Епархия", 1500f))
            add(Book(gimmeId(), "Котлин и КТор", "Валерий П.", 500f))
        }
    }

    fun newBook(book: Book) {
        books.add(book)
    }

    fun updateBook(book: Book): Book? {
        val findBook = books.find {
            it.id == book.id
        }
        findBook?.let {
            it.author = book.author
            it.title = book.title
            it.price = book.price
        }
        return findBook
    }

    fun allBooks(): ArrayList<Book> {
        return books
    }

    fun deleteBook(bookId: String): Book? {
        val findBook = books.find {
            it.id == bookId
        }
        findBook?.let { books.remove(it) }
        return findBook
    }

    fun sortedBooksReflect(sortBy: String, asc: Boolean): List<Book> {
        val member = Book::class.declaredMemberProperties.find { it.name == sortBy }
        if (member == null) {
            log.info("The field to sort by does not exist")
            return allBooks()
        }

        log.info("Type member: ${member.returnType}")

        return if (sortBy == "price") {
            if (asc) allBooks().sortedBy { it.price }
            else allBooks().sortedByDescending { it.price }
        } else {
            if (asc) allBooks().sortedBy { member.get(it).toString() }
            else allBooks().sortedByDescending { member.get(it).toString() }
        }
    }

    fun sortedBooks(sortBy: String, asc: Boolean): List<Book> {
        return if (asc) {
            when (sortBy.lowercase()) {
                "title" -> books.sortedBy { it.title.lowercase() }
                "author" -> books.sortedBy { it.author.lowercase() }
                "price" -> books.sortedBy { it.price }
                else -> books
            }
        } else {
            when (sortBy.lowercase()) {
                "title" -> books.sortedByDescending { it.title.lowercase() }
                "author" -> books.sortedByDescending { it.author.lowercase() }
                "price" -> books.sortedByDescending { it.price }
                else -> books
            }
        }
    }
}