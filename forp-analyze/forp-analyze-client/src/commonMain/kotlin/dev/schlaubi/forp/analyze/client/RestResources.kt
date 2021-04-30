package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.client.retry.Retry
import dev.schlaubi.forp.analyze.remote.ForpModule
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext

internal class RestResources(
    val authKey: String,
    val url: Url,
    val retry: Retry,
    clientEngine: HttpClientEngineFactory<*>? = null,
    override val coroutineContext: CoroutineContext,
) : CoroutineScope {

    val json = Json {
        serializersModule = ForpModule
    }

    val httpClient: HttpClient

    init {
        val configure: HttpClientConfig<*>.() -> Unit = {
            defaultRequest {
                headers[HttpHeaders.Authorization] = "Bearer $authKey"
            }

            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }

            install(WebSockets)
        }

        httpClient = if (clientEngine != null) {
            HttpClient(clientEngine, configure)
        } else {
            HttpClient(configure)
        }
    }

}