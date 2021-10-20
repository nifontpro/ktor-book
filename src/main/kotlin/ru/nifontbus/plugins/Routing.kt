package ru.nifontbus.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.nifontbus.books

@KtorExperimentalLocationsAPI
fun Application.configureRouting() {

    routing {

        install(Locations)

        books()

        get("/") {
            call.respondText("Hello World!")
        }

        authenticate("bookStoreAuth") {
            get("/api/tryauth") {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respondText("Hello ${principal.name}")
            }
        }

        install(StatusPages) {

            exception<Throwable> { cause ->
                call.respond(HttpStatusCode.InternalServerError)
                throw cause
            }

            status(HttpStatusCode.NotFound) {
                log.error("--------------- NOT FOUND!")
                call.respond(
                    TextContent(
                        "${it.value} ${it.description}",
                        ContentType.Text.Plain.withCharset(Charsets.UTF_8),
                        it
                    )
                )
            }

        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
    }
}