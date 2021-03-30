@file:JvmName("Events")

package dev.schlaubi.forp.analyze

import dev.schlaubi.forp.analyze.events.Event
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

/**
 * Listener for an [Event]
 *
 * @param T the type of the [Event] this Listener can listen to
 */
public fun interface EventListener<T : Event> {

    /**
     * Called whenever the [event] [T] is fired.
     *
     * **Note**: [onEvent] calls this in a separate thread.
     */
    public fun onEvent(event: T)
}

public class JavaStackTraceAnalyzer(private val parent: StackTraceAnalyzer) {
    public fun createNewConversationAsync(): CompletableFuture<Conversation> =
        parent.future { parent.createNewConversation() }

    public fun createNewConversation(): Conversation =
        createNewConversationAsync().join()

}

/**
 * Adds an [EventListener] to this conversation to listen for [event].
 *
 * In order to cancel the listener call [Job.cancel]
 */
public fun <T : Event> Conversation.onEvent(event: Class<T>, listener: EventListener<T>): Job {
    val kClass = event.kotlin

    return events.buffer(Channel.UNLIMITED).filter { kClass.isInstance(it) }
        .onEach {
            launch {
                @Suppress("UNCHECKED_CAST")
                listener.onEvent(it as T)
            }
        }
        .launchIn(this)
}
