package dev.schlaubi.forp.analyze.core

import dev.schlaubi.forp.analyze.Conversation
import dev.schlaubi.forp.analyze.core.utils.ClassFinder
import dev.schlaubi.forp.analyze.events.Event
import dev.schlaubi.forp.analyze.events.ExceptionFoundEvent
import dev.schlaubi.forp.analyze.events.SourceFileFoundEvent
import dev.schlaubi.forp.fetch.input.Input
import dev.schlaubi.forp.fetch.processor.Result
import dev.schlaubi.forp.parser.stacktrace.RootStackTrace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal class ConversationImpl(
    private val analyzer: StackTraceAnalyzerImpl,
    override val id: Long
) : Conversation, CoroutineScope by analyzer {

    private val eventFlow: MutableSharedFlow<Event> = MutableSharedFlow(
        extraBufferCapacity = Int.MAX_VALUE
    )

    override val events: SharedFlow<Event> = eventFlow.asSharedFlow()

    override fun consumeNewInput(input: Input) {
        launch {
            analyzer.fetch(input).forEach {
                val result = it.result
                processResult(it)
                if (result != null) {
                    processNewException(result)
                }
            }
        }
    }

    private fun processResult(result: Result) {
        launch {
            ClassFinder.findClasses(result.raw).forEach {
                eventFlow.emit(SourceFileFoundEvent(it))
            }
        }
    }

    private suspend fun processNewException(exception: RootStackTrace) {
        // TODO: javadoc
        eventFlow.emit(ExceptionFoundEvent(exception))
    }

    override fun forget() {
        analyzer.forget(id)
    }
}
