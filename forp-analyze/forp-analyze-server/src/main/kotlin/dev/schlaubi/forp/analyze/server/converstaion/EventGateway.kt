package dev.schlaubi.forp.analyze.server.converstaion

import dev.schlaubi.forp.analyze.events.Event
import dev.schlaubi.forp.analyze.remote.RemoteEvent
import dev.schlaubi.forp.analyze.server.Application
import dev.schlaubi.forp.analyze.server.auth.forp
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString

class EventGateway {

    private val sessions = mutableMapOf<String, DefaultWebSocketServerSession>()

    suspend fun reportEvent(conversation: APIConversation, event: RemoteEvent) {
        val session = sessions[conversation.token] ?: error("Could not find session")
        val json = Application.json.encodeToString(event)

        session.send(Frame.Text(json))
    }

    fun Route.apply() {
        webSocket("/ws") {
            val (token) = call.forp()

            sessions[token]?.dropOldConnection()

            sessions[token] = this
        }
    }

    private suspend fun DefaultWebSocketServerSession.send(event: Event) = outgoing.send(Frame.Text(
        Application
            .json
            .encodeToString(event)
    ))

    private suspend fun DefaultWebSocketServerSession.dropOldConnection() {
        close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Logged in from another location"))
    }
}
