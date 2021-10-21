package ru.nifontbus

import io.ktor.application.*
import io.ktor.locations.*
import ru.nifontbus.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalLocationsAPI
@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureMonitoring()
    configureSecurity()
    configureRouting()
    configureSerialization()
    configureHTTP()
    configureTemplating()
}
