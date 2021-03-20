package dev.schlaubi.forp.fetch

import dev.schlaubi.forp.core.stacktrace.RootStackTrace
import dev.schlaubi.forp.fetch.input.Input
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

/**
 * Wrapper for [StackTraceFetcher] that doesn't rely on coroutines.
 *
 * @see StackTraceFetcherBuilder.buildJava
 */
@Suppress("MemberVisibilityCanBePrivate")
public class JavaStackTraceFetcher internal constructor(
    private val delegate: StackTraceFetcher,
    dispatcher: ExecutorService
) : CoroutineScope {
    override val coroutineContext: ExecutorCoroutineDispatcher = dispatcher.asCoroutineDispatcher()

    /**
     * Fetches all [inputs] asynchronously and in parallel.
     *
     * @return a [CompletableFuture] containing all [RootStackTrace]s found
     */
    public fun fetchAsync(vararg inputs: Input): CompletableFuture<List<RootStackTrace>> =
        future { delegate.fetch(*inputs) }

    /**
     * Fetches all [inputs] synchronously (blocks this thread) and in parallel.
     *
     * @see CompletableFuture.join
     * @see fetchAsync
     * @return a [List] containing all [RootStackTrace]s found
     */
    public fun fetch(vararg inputs: Input): List<RootStackTrace> =
        fetchAsync(inputs.asIterable()).join()

    /**
     * Fetches all [inputs] asynchronously and in parallel.
     *
     * @return a [CompletableFuture] containing all [RootStackTrace]s found
     */
    public fun fetchAsync(inputs: Iterable<Input>): CompletableFuture<List<RootStackTrace>> =
        future { delegate.fetch(inputs) }

    /**
     * Fetches all [inputs] synchronously (blocks this thread) and in parallel.
     *
     * @see CompletableFuture.join
     * @see fetchAsync
     * @return a [List] containing all [RootStackTrace]s found
     */
    public fun fetch(inputs: Iterable<Input>): List<RootStackTrace> =
        fetchAsync(inputs).join()

    /**
     * Fetches [input] asynchronously.
     *
     * @return a [CompletableFuture] containing all [RootStackTrace]s found
     */
    public fun fetchAsync(input: Input): CompletableFuture<List<RootStackTrace>> =
        future { delegate.fetch(input) }

    /**
     * Fetches [input] synchronously (blocks this thread).
     *
     * @see CompletableFuture.join
     * @see fetchAsync
     * @return a [List] containing all [RootStackTrace]s found
     */
    public fun fetch(input: Input): List<RootStackTrace> =
        fetchAsync(input).join()

    /**
     * Closes all resources the fetcher needed.
     */
    public fun close() {
        delegate.close()
        coroutineContext.close()
    }
}
