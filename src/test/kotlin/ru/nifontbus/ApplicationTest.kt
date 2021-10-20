package ru.nifontbus

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.auth.*
import io.ktor.util.*
import org.slf4j.event.*
import io.ktor.sessions.*
import io.ktor.content.*
import io.ktor.http.content.*
import io.ktor.html.*
import kotlinx.html.*
import kotlinx.css.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import ru.nifontbus.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Hello World!", response.content)
            }
        }
    }
}