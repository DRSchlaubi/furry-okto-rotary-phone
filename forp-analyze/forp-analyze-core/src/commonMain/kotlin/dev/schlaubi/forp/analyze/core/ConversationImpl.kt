package dev.schlaubi.forp.analyze.core

import dev.schlaubi.forp.analyze.Conversation
import dev.schlaubi.forp.analyze.core.events.ExceptionFoundEventImpl
import dev.schlaubi.forp.analyze.core.events.JavaDocFoundEventImpl
import dev.schlaubi.forp.analyze.core.events.SourceFileFoundEventImpl
import dev.schlaubi.forp.analyze.core.utils.ClassFinder
import dev.schlaubi.forp.analyze.events.Event
import dev.schlaubi.forp.analyze.events.ExceptionFoundEvent
import dev.schlaubi.forp.analyze.events.JavaDocFoundEvent
import dev.schlaubi.forp.analyze.events.SourceFileFoundEvent
import dev.schlaubi.forp.analyze.javadoc.DocumentedClassObject
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
            println("fetching")
            analyzer.fetch(input).forEach {
                val result = it.result
                println("Got result")
                processResult(it)
                if (result != null) {
                    println("Processing stacktrace")
                    processNewException(result)
                }
            }
        }
    }

    private fun processResult(result: Result) {
        launch {
            ClassFinder.findClasses(result.raw).forEach {
                eventFlow.emit(SourceFileFoundEventImpl(it))
            }
        }
    }

    private suspend fun processNewException(exception: RootStackTrace) {
        eventFlow.emit(ExceptionFoundEventImpl(exception))

        val exceptions = (exception.children.map { it.exception } + exception.exception)
            .distinct()
        exceptions.forEach {
            val doc = analyzer.javadocs.findDoc(
                exception.exception
            ) as? DocumentedClassObject

            if (doc == null) {
                println("No doc found for $it")
                println("Potential: $doc")
            } else {
                eventFlow.emit(JavaDocFoundEventImpl(it, doc))
            }

        }
    }

    override fun forget() {
        analyzer.forget(id)
    }
}
