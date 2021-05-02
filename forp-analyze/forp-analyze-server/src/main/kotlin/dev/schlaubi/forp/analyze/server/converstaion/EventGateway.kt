package dev.schlaubi.forp.analyze.server.converstaion

import dev.schlaubi.forp.analyze.remote.RemoteEvent
import dev.schlaubi.forp.analyze.server.Application
import dev.schlaubi.forp.analyze.server.auth.forp
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import mu.KotlinLogging


private val LOG = KotlinLogging.logger { }

class EventGateway {

    private val sessions = mutableMapOf<String, DefaultWebSocketServerSession>()

    suspend fun reportEvent(conversation: APIConversation, event: RemoteEvent) {
        val session = sessions[conversation.token] ?: error("Could not find session")
        val json = Application.json.encodeToString(event)

        LOG.debug { "Sending $json to $session" }

        session.send(event)
    }

    fun Route.apply() {
        webSocket("/ws") {
            val (token) = call.forp()

            sessions[token]?.dropOldConnection()

            sessions[token] = this

            LOG.debug { "New connection: $this" }

            for (frame in incoming) {
                LOG.debug { "Received $frame" }
            }
        }
    }

    private suspend fun DefaultWebSocketServerSession.send(event: RemoteEvent) = outgoing.send(
        Frame.Text(
            Application
                .json
                .encodeToString(event)
        )
    )

    private suspend fun DefaultWebSocketServerSession.dropOldConnection() {
        close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Logged in from another location"))
    }
}
