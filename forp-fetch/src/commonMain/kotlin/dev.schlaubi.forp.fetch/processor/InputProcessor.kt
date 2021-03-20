package dev.schlaubi.forp.fetch.processor

import dev.schlaubi.forp.fetch.input.Input

/**
 * Tool for converting an [Input] into one or more Strings representing a stack trace.
 *
 * @param I the type of the [Input] the processor can process
 * @param O An optional wrapper type that can be used between processing and fetching (e.g. Input -> Url -> string>
 */
public interface InputProcessor<I : Input, O> {

    /**
     * Whether this processor supports [input] or not
     */
    public fun supports(input: Input): Boolean

    /**
     * Searches the [input] for any possible references to stacktraces (e.g. URLs) or the stacktrace itself
     */
    public fun processInput(input: I): O

    /**
     * Converts [O] to a List of found [String]s.
     */
    public suspend fun fetchInput(data: O): List<String>
}

/**
 * Helper extension of [InputProcessor] for processors which can find more than one input.
 */
public interface MultiInputProcessor<I : Input, T> : InputProcessor<I, List<T>>
