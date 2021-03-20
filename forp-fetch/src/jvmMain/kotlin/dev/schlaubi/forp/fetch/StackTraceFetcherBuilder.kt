package dev.schlaubi.forp.fetch

import dev.schlaubi.forp.fetch.processor.InputProcessor
import dev.schlaubi.forp.fetch.stackTraceFetcher
import io.ktor.client.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * [FunctionalInterface] which provides an [InputProcessor].
 *
 * Example:
 * ```
 * addHttpProcessor(client -> new HastebinClient(client, "hastebin.com")
 * ```
 */
public fun interface HttpProcessorProvider {
    public fun addProcessor(httpClient: HttpClient): InputProcessor<*, *>
}

/**
 * JVM Implementation of [AbstractStackTraceFetcherBuilder].
 * @see AbstractStackTraceFetcherBuilder
 */
public actual class StackTraceFetcherBuilder : AbstractStackTraceFetcherBuilder() {

    /**
     * Adds an [InputProcessor] that requires a [HttpClient].
     *
     * ```kotlin
     * addHttpFetcher(client -> new HastebinProcessor(client, "hastebin.com")
     * ```
     */
    public fun addHttpFetcher(provider: HttpProcessorProvider): StackTraceFetcherBuilder =
        +provider.addProcessor(defaultHttpClient)

    /**
     * This builds a [JavaStackTraceFetcher].
     *
     * For Kotlin please use [stackTraceFetcher]
     */
    @JvmName("build")
    @JvmOverloads
    public fun buildJava(
        executor: ExecutorService = Executors.newCachedThreadPool()
    ): JavaStackTraceFetcher = JavaStackTraceFetcher(build(), executor)
}
