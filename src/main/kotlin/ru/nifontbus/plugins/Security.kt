package ru.nifontbus.plugins

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlin.collections.listOf
import kotlin.collections.set

fun Application.configureSecurity() {

    val users = listOf("nifont", "shopper")
    val admins = listOf("admin")

    authentication {
        basic(name = "bookStoreAuth") {
            realm = "Book Store"
            validate { credentials ->
                if (users.contains(credentials.name) || admins.contains(credentials.name)
                    && credentials.password == "password"
                ) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }

    }

    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    routing {
        get("/session/increment") {
            val session = call.sessions.get<MySession>() ?: MySession()
            call.sessions.set(session.copy(count = session.count + 1))
            call.respondText("Counter is ${session.count}. Refresh to increment.")
        }
    }
}
