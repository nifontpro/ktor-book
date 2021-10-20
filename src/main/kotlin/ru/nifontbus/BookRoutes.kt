package ru.nifontbus

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
@Location("api/book/list")
data class BookListLocation(val sortby: String, val asc: Boolean)

@KtorExperimentalLocationsAPI
fun Route.books() {

    val dataManager = DataManager()

    authenticate("bookStoreAuth") {
        get<BookListLocation> {
            call.respond(dataManager.sortedBooks(it.sortby, it.asc))
        }
    }

    route("/book") {
        get("/") {
            call.respond(dataManager.allBooks())
        }

        post("/{id}") {
//            val id = call.parameters["id"]
            val book = call.receive<Book>()
            val updateBook = dataManager.updateBook(book)
            updateBook?.let { call.respond(it) }
        }

        put("") {
            val book = call.receive<Book>()
            val newBook = dataManager.newBook(book)
            call.respond(newBook)
        }

        delete("/{id}") {
            val id = call.parameters["id"].toString()
            val deleteBook = dataManager.deleteBook(id)
            deleteBook?.let { call.respond(it) }
        }
    }
}