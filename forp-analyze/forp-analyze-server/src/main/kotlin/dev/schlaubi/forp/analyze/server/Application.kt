package dev.schlaubi.forp.analyze.server

import dev.schlaubi.forp.analyze.remote.ForpModule
import dev.schlaubi.forp.analyze.server.auth.authenticated
import dev.schlaubi.forp.analyze.server.auth.forp
import dev.schlaubi.forp.analyze.server.converstaion.EventGateway
import dev.schlaubi.forp.analyze.server.converstaion.conversations
import dev.schlaubi.forp.analyze.server.errors.installErrorHandler
import dev.schlaubi.forp.analyze.server.javadoc.javadocs
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import kotlin.coroutines.CoroutineContext
import io.ktor.application.Application as KtorApp

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

object Application : CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO + SupervisorJob()

    val json = Json {
        serializersModule = ForpModule
    }

    val config = dev.schlaubi.forp.analyze.server.config.Config()

    val analyzer = config.buildAnalyzer()

    val webSocket = EventGateway()

    @Suppress("unused", "UNUSED_PARAMETER")
    @JvmStatic
    @JvmOverloads
    fun KtorApp.module(testing: Boolean = false) {
        install(ContentNegotiation) {
            json(json)
        }

        install(Locations)
        install(WebSockets)
        install(DefaultHeaders)
        install(CallLogging) {
            logger = LoggerFactory.getLogger("RequestLogger")
            level = Level.DEBUG
        }

        install(Authentication) {
            forp()
        }

        installErrorHandler()

        routing {
            authenticated {
                conversations()
                javadocs()
                with(webSocket) {
                    apply()
                }
            }
        }
    }
}
