package dev.schlaubi.forp.analyze.server

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.Feature
import com.uchuhimo.konf.source.toml
import dev.schlaubi.forp.analyze.core.stackTraceAnalyzer
import dev.schlaubi.forp.analyze.javadoc.useDocDex
import dev.schlaubi.forp.analyze.remote.ForpModule
import dev.schlaubi.forp.analyze.server.auth.authenticated
import dev.schlaubi.forp.analyze.server.auth.forp
import dev.schlaubi.forp.analyze.server.config.ForpConfigSpec
import dev.schlaubi.forp.analyze.server.converstaion.EventGateway
import dev.schlaubi.forp.analyze.server.converstaion.conversations
import dev.schlaubi.forp.analyze.server.errors.installErrorHandler
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import dev.schlaubi.forp.analyze.server.config.Config as ServerConfig
import io.ktor.application.Application as KtorApp

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

object Application : CoroutineScope {

    override val coroutineContext: CoroutineContext = Dispatchers.IO + Job()

    val json = Json {
        serializersModule = ForpModule
    }

    private val konfig = Config {
        addSpec(ForpConfigSpec)
        enable(Feature.FAIL_ON_UNKNOWN_PATH)
        from.toml.file("config.toml")
    }

    val config = ServerConfig(konfig)

    val analyzer = config.buildAnalyzer()

    val webSocket = EventGateway()

    @JvmStatic
    @JvmOverloads
    fun KtorApp.module(testing: Boolean = false) {
        install(ContentNegotiation) {
            json(json)
        }

        install(Locations)
        install(WebSockets)

        install(Authentication) {
            forp()
        }

        installErrorHandler()

        routing {
            authenticated {
                conversations()
                with(webSocket) {
                    apply()
                }
            }
        }
    }
}
