package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.remote.RemoteEvent
import io.ktor.client.features.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import mu.KotlinLogging

private val LOG = KotlinLogging.logger {}

internal class WebsocketClient(private val resources: RestResources) {
    @Suppress("PropertyName")
    val _events: MutableSharedFlow<RemoteEvent> =
        MutableSharedFlow(extraBufferCapacity = Int.MAX_VALUE)
    val events: SharedFlow<RemoteEvent> = _events.asSharedFlow()
    private lateinit var session: DefaultClientWebSocketSession

    init {
        events.onEach {
            println(it)
        }.launchIn(GlobalScope)
    }

    internal suspend fun connect(resume: Boolean = false) {
        session = try {
            resources.httpClient.webSocketSession {
                url(URLBuilder(resources.url).apply {
                    protocol = if (protocol.isSecure()) URLProtocol.WSS else URLProtocol.WS
                    path("ws")
                }.build())
            }
        } catch (e: ServerResponseException) {
            reconnect(
                IllegalArgumentException(
                    "The provided server responded with an invalid response code",
                    e
                ), resume
            )
            return
        } catch (e: ConnectTimeoutException) {
            reconnect(IllegalStateException("The connection to the node timed out", e), resume)
            return
        } catch (e: ClientRequestException) {
            reconnect(e, resume)
            return
        }
        resources.retry.reset()

        LOG.debug { "Successfully connected to gateway: (${session.call.request.url})" }

        for (message in session.incoming) {
            when (message) {
                is Frame.Text -> onMessage(message)
                else -> {
                    LOG.warn { "Received unexpected websocket frame: $message" }
                }
            }
        }
        val reason = session.closeReason.await()
        if (reason?.knownReason == CloseReason.Codes.NORMAL) return
        LOG.warn { "Disconnected from websocket for: $reason. Will try to reconnect" }
        reconnect(resume = true)
    }

    private suspend fun reconnect(e: Throwable? = null, resume: Boolean = false) {
        LOG.error(e) { "Error whilst trying to connect. Reconnecting" }
        if (resources.retry.hasNext) {
            resources.retry.retry()
            connect(resume)
        } else {
            error("Could not reconnect to websocket after to many attempts")
        }
    }

    private suspend fun onMessage(message: Frame.Text) {
        val string = message.readText()
        LOG.debug { "Received event: $string" }
        val event = resources.json.decodeFromString<RemoteEvent>(string)

        _events.emit(event)
    }

    suspend fun close() {
        session.close(CloseReason(CloseReason.Codes.GOING_AWAY, "Client disconnect"))
    }
}
