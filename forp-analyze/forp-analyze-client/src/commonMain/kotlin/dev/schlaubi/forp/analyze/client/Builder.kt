package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.StackTraceAnalyzer
import dev.schlaubi.forp.analyze.client.retry.LinearRetry
import dev.schlaubi.forp.analyze.client.retry.Retry
import io.ktor.client.engine.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

public class RemoteStackTraceAnalyzerBuilder {

    public lateinit var authKey: String
    public lateinit var url: Url
    public var dispatcher: CoroutineContext = Dispatchers.Default
    public var retry: Retry = LinearRetry()
    public var httpEngine: HttpClientEngineFactory<*>? = null

    public fun build(): StackTraceAnalyzer {
        require(::authKey.isInitialized) { "Auth key may not be null" }
        require(::url.isInitialized) { "Auth key may not be null" }

        val resources = RestResources(authKey, url, retry, httpEngine, dispatcher)

        val analyzer = RemoteStackTraceAnalyzer(resources, dispatcher)
        analyzer.javadocs.prepare()
        return analyzer
    }
}