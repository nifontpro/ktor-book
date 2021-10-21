package ru.nifontbus

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.nifontbus.model.Book
import ru.nifontbus.model.DataManagerMongoDB
import kotlin.math.log

@KtorExperimentalLocationsAPI
@Location("api/book/list")
data class BookListLocation(val sortby: String, val page: Int)

@KtorExperimentalLocationsAPI
fun Route.books() {

    val dataManager = DataManagerMongoDB()

    authenticate("bookStoreAuth") {

        get<BookListLocation> {
            call.respond(dataManager.sortedBooks(it.sortby, it.page))
        }
    }

    route("/book") {
        get("/") {
            call.respond(dataManager.allBooks())
        }

        post("/{id}") {
            val id = call.parameters["id"]!!
            val book = call.receive<Book>()
            val updateBook = dataManager.updateBook(id, book)
            updateBook?.let { call.respond(it) } ?: call.respondText("Not Found")
        }

        put("") {
            val book = call.receive<Book>()
            val newBook = dataManager.newBook(book)
            call.respond(newBook)
        }

        delete("/{id}") {
            val id = call.parameters["id"]!!
            println("-----> id = $id")
            val deleteBook = dataManager.deleteBook(id)
            deleteBook?.let { call.respond(it) } ?: call.respondText("Not Found")
        }
    }
}