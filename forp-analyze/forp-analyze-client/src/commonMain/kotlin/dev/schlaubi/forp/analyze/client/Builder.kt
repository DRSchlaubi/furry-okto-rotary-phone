package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.StackTraceAnalyzer
import dev.schlaubi.forp.analyze.client.retry.LinearRetry
import dev.schlaubi.forp.analyze.client.retry.Retry
import io.ktor.client.engine.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Creates a new [StackTraceAnalyzer] and applies [builder] to it.
 */
@Suppress("FunctionName")
public fun RemoteStackTraceAnalyzer(
    builder: RemoteStackTraceAnalyzerBuilder.() -> Unit = {}
): StackTraceAnalyzer =
    RemoteStackTraceAnalyzerBuilder().apply(builder).build()

/**
 * Builder for a remote [StackTraceAnalyzer].
 *
 * @property authKey they key for authenticating with the node
 * @property url the [Url] of the server
 * @property dispatcher the [Dispatchers] for communication coroutines
 * @property retry the [reconnecting strategy][Retry]
 * @property httpEngine a [HttpClientEngineFactory] for communication
 */
public class RemoteStackTraceAnalyzerBuilder {

    public lateinit var authKey: String
    public lateinit var serverUrl: Url
    public var dispatcher: CoroutineContext = Dispatchers.Default
    public var retry: Retry = LinearRetry()
    public var httpEngine: HttpClientEngineFactory<*>? = null

    /**
     * Sets the host url to [url].
     */
    public fun url(url: String) {
        serverUrl = Url(url)
    }

    /**
     * Builds a host url using [builder].
     */
    public fun url(builder: URLBuilder.() -> Unit) {
        serverUrl = URLBuilder().apply(builder).build()
    }

    /**
     * Builds the [StackTraceAnalyzer].
     */
    public fun build(): StackTraceAnalyzer {
        require(::authKey.isInitialized) { "Auth key may not be null" }
        require(::serverUrl.isInitialized) { "Auth key may not be null" }

        val resources = RestResources(authKey, serverUrl, retry, httpEngine, dispatcher)

        val analyzer = RemoteStackTraceAnalyzer(resources, dispatcher)
        analyzer.javadocs.prepare()
        return analyzer
    }
}
