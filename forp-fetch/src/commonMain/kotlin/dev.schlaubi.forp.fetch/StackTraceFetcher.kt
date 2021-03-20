package dev.schlaubi.forp.fetch

import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import dev.schlaubi.forp.fetch.input.Input
import dev.schlaubi.forp.fetch.processor.InputProcessor
import dev.schlaubi.forp.fetch.utils.asyncMap
import dev.schlaubi.forp.find.StackTraceFinder
import io.ktor.client.*
import kotlin.jvm.JvmStatic

/**
 * Fetcher than can fetch StackTraces.
 *
 * @see newBuilder
 * @see stackTraceFetcher
 */
public class StackTraceFetcher internal constructor(
    private val processors: List<InputProcessor<*, *>>,
    private val defaultHttpClient: Lazy<HttpClient>
) {

    /**
     * Fetches all [inputs] in parallel.
     */
    public suspend fun fetch(vararg inputs: Input): List<RootStackTrace> =
        fetch(inputs.asIterable())

    /**
     * Fetches all [inputs] in parallel.
     */
    public suspend fun fetch(inputs: Iterable<Input>): List<RootStackTrace> =
        inputs.asyncMap(::fetch).flatten()

    /**
     * Fetches the [input] by searching for [RootStackTrace] or references to them.
     */
    public suspend fun fetch(input: Input): List<RootStackTrace> {
        val inputs = process(input)

        return inputs.map {
            StackTraceFinder.findStackTraces(it)
        }.flatten()
    }

    /**
     * Closes all resources the fetcher needed.
     */
    public fun close() {
        if (defaultHttpClient.isInitialized()) {
            defaultHttpClient.value.close()
        }
    }

    private suspend fun process(input: Input): List<String> {
        val availableProcessors = processors.filterCompatible(input)
        val processedInputs = availableProcessors.process(input)

        return processedInputs.fetch()
    }

    private suspend fun <I : Input> List<Pair<Any?, InputProcessor<I, *>>>.fetch(): List<String> =
        asyncMap {
            val (processedInput, processor) = it
            @Suppress("UNCHECKED_CAST")
            (processor as InputProcessor<I, Any?>).fetchInput(processedInput)
        }.flatten()

    private fun <I : Input> List<InputProcessor<I, *>>.process(input: I) =
        map {
            it.processInput(input) to it
        }

    private fun <T : Input> List<InputProcessor<*, *>>.filterCompatible(input: T) =
        asSequence()
            .filter { it.supports(input) }
            .map {
                @Suppress("UNCHECKED_CAST")
                it as InputProcessor<T, *>
            }
            .toList()

    public companion object {

        /**
         * Creates a new [StackTraceFetcherBuilder].
         *
         * Kotlin users please refer to [stackTraceFetcher]
         */
        @JvmStatic
        public fun newBuilder(): StackTraceFetcherBuilder = StackTraceFetcherBuilder()
    }
}
