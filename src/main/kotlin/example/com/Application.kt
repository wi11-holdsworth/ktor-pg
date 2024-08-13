package example.com

import example.com.db.DatabaseFactory
import example.com.plugins.configureRouting
import example.com.plugins.configureSerialization
import example.com.routes.configureTaskRoutes
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init(environment.config)
    configureSerialization()
    configureTaskRoutes()
    configureRouting()
}
