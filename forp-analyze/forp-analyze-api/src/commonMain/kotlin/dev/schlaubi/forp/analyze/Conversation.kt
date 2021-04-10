package dev.schlaubi.forp.analyze

import dev.schlaubi.forp.analyze.events.Event
import dev.schlaubi.forp.fetch.input.Input
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * A conversation represents an ongoing stream of inputs from a user.
 */
public interface Conversation : CoroutineScope {

    /**
     * An id to identify this Conversation.
     */
    public val id: Long

    /**
     * A [Flow] of [Events][Event].
     *
     * @see on
     */
    public val events: Flow<Event>

    /**
     * Instructs the analyzer to process a new [input].
     *
     * @see Input
     */
    public fun consumeNewInput(input: Input)

    /**
     * Cancels any further processing of this conversation.
     */
    public fun forget()

}

/**
 * Convenience method that will invoke the [consumer] on every event [E] created by [Conversation.events].
 *
 * The events are buffered in an [unlimited][Channel.UNLIMITED] [buffer][Flow.buffer] and
 * [launched][CoroutineScope.launch] in the supplied [scope], which is [Conversation] by default.
 * Each event will be [launched][CoroutineScope.launch] inside the [scope] separately and
 * any thrown [Throwable] will be caught and logged.
 *
 * The returned [Job] is a reference to the created coroutine, call [Job.cancel] to cancel the processing of any further
 * events.
 */
public inline fun <reified E : Event> Conversation.on(
    scope: CoroutineScope = this,
    noinline consumer: suspend E.() -> Unit,
): Job =
    events.buffer(Channel.UNLIMITED).filterIsInstance<E>()
        .onEach {
            scope.launch {
                consumer(it)
            }
        }
        .launchIn(scope)
