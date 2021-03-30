package dev.schlaubi.forp.analyze.core

import dev.schlaubi.forp.analyze.StackTraceAnalyzer
import dev.schlaubi.forp.fetch.StackTraceFetcher
import dev.schlaubi.forp.fetch.StackTraceFetcherBuilder
import dev.schlaubi.forp.fetch.stackTraceFetcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

public inline fun stackTraceAnalyzer(builder: StackTraceAnalyzerBuilder.() -> Unit): StackTraceAnalyzer =
    StackTraceAnalyzerBuilder().apply(builder).build()

/**
 * Please see [AbstractStackTraceAnalyzerBuilder] for the API.
 */
public expect class StackTraceAnalyzerBuilder() : AbstractStackTraceAnalyzerBuilder

/**
 * API for [StackTraceAnalyzerBuilder].
 */
public abstract class AbstractStackTraceAnalyzerBuilder {

    /**
     * The [StackTraceFetcher] which is used to analyze the inputs.
     */
    public var fetcher: StackTraceFetcher? = null

    public open var coroutineDispatcher: CoroutineContext = Dispatchers.Default + Job()

    /**
     * Shortcut to fetcher = [stackTraceFetcher].
     */
    public fun fetcher(builder: StackTraceFetcherBuilder.() -> Unit) {
        fetcher = stackTraceFetcher(builder)
    }

    /**
     * Please use [stackTraceAnalyzer].
     */
    @PublishedApi
    @JvmName("buildKotlin")
    internal fun build(): StackTraceAnalyzer {
        val fetcher = fetcher
        requireNotNull(fetcher) { "Fetcher may not be null" }

        return StackTraceAnalyzerImpl(fetcher, coroutineDispatcher)
    }
}
