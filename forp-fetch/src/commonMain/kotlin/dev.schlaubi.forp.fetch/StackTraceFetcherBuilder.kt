package dev.schlaubi.forp.fetch

import dev.schlaubi.forp.fetch.processor.InputProcessor
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlin.jvm.JvmName
import kotlin.jvm.JvmSynthetic

public fun stackTraceFetcher(builder: StackTraceFetcherBuilder.() -> Unit): StackTraceFetcher =
    StackTraceFetcherBuilder().apply(builder).build()

/**
 * Builder for [StackTraceFetcher].
 *
 * @see StackTraceFetcher.Companion.newBuilder
 * @see stackTraceFetcher
 */
@Suppress("MemberVisibilityCanBePrivate")
public abstract class AbstractStackTraceFetcherBuilder internal constructor() {

    private val processors = mutableListOf<InputProcessor<*, *>>()

    private val defaultHttpClientDelegate = lazy {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    /**
     * Default [HttpClient] which can be used for all fetching processors.
     *
     * This will be closed automatically by [StackTraceFetcher.close]
     */
    public val defaultHttpClient: HttpClient by defaultHttpClientDelegate

    /**
     * This adds an [InputProcessor] to this fetcher.
     * For Kotlin users this is intended to be used like this:
     * ```kotlin
     * stackTraceFetcher {
     *  +PlainStringProcessor()
     * }
     * ```
     *
     * For Java users this is intended to be used like this:
     * ```java
     * StackTraceFetcher.newBuilder()
     *  .addProcessor(new PlainStringProcessor())
     *  .build()
     *  ```
     */
    @JvmName("addProcessor")
    public operator fun InputProcessor<*, *>.unaryPlus(): StackTraceFetcherBuilder {
        processors.add(this)
        return this@AbstractStackTraceFetcherBuilder as StackTraceFetcherBuilder
    }

    /**
     * Adds all [processors] to this fetcher.
     */
    public fun addProcessors(vararg processors: InputProcessor<*, *>): Boolean =
        addProcessors(processors.asIterable())

    /**
     * Adds all [processors] to this fetcher.
     */
    public fun addProcessors(processors: Iterable<InputProcessor<*, *>>): Boolean =
        this.processors.addAll(processors)

    /**
     * Use [stackTraceFetcher].
     */
    @Suppress("UNCHECKED_CAST")
    @JvmSynthetic
    @JvmName("buildKotlin")
    internal fun build(): StackTraceFetcher = StackTraceFetcher(
        processors.toList(),
        defaultHttpClientDelegate
    )
}

/**
 * This is only a artifact of MPP.
 * @see AbstractStackTraceFetcherBuilder
 */
public expect class StackTraceFetcherBuilder internal constructor() :
    AbstractStackTraceFetcherBuilder
