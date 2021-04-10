package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.Conversation
import dev.schlaubi.forp.analyze.events.Event
import dev.schlaubi.forp.fetch.input.FileInput
import dev.schlaubi.forp.fetch.input.Input
import dev.schlaubi.forp.fetch.input.PlainStringInput
import dev.schlaubi.forp.fetch.input.StringInput
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class RemoteConversation(
    override val id: Long,
    private val analyzer: RemoteStackTraceAnalyzer,
) : Conversation {

    override val coroutineContext: CoroutineContext
        get() = analyzer.coroutineContext

    override val events: Flow<Event> = analyzer.websocket.events
        .filter { it.conversationId == id }
        .map { it.data }

    override fun consumeNewInput(input: Input) {
        launch {
            when (input) {
                is StringInput -> analyzer.service.uploadPlainText(
                    id, false, input.input
                )

                is PlainStringInput -> analyzer.service.uploadPlainText(
                    id, true, input.input
                )

                is FileInput -> {
                    analyzer.service.uploadFile(id, input)
                }
            }
        }
    }

    override fun forget() {
        launch {
            analyzer.service.forget(id)
        }
    }
}
