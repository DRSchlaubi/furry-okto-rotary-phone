@file:JvmName("DocDexUtils")

package dev.schlaubi.forp.analyze.javadoc

import dev.schlaubi.forp.analyze.core.StackTraceAnalyzerBuilder
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Uses a [Docdex](https://github.com/PiggyPiglet/DocDex) powered [JavaDocCache]
 *
 * @param httpClientEngine the [HttpClientEngine] used to create a http client used for communication
 * @param url the url to the docdex instance
 * @param ignoreDocs a list of [docdex names](https://github.com/PiggyPiglet/DocDex/wiki/Running-The-App#configuration) to ignore during cache population
 */

@JvmOverloads
public fun StackTraceAnalyzerBuilder.useDocDex(
    httpClientEngine: HttpClientEngine? = null,
    url: String,
    vararg ignoreDocs: String,
): StackTraceAnalyzerBuilder = useDocDex(httpClientEngine, Url(url), *ignoreDocs)

/**
 * Uses a [Docdex](https://github.com/PiggyPiglet/DocDex) powered [JavaDocCache]
 *
 * @param httpClientEngine the [HttpClientEngine] used to create a http client used for communication
 * @param url the [Url] to the docdex instance
 * @param ignoreDocs a list of [docdex names](https://github.com/PiggyPiglet/DocDex/wiki/Running-The-App#configuration) to ignore during cache population
 */
@JvmOverloads
public fun StackTraceAnalyzerBuilder.useDocDex(
    httpClientEngine: HttpClientEngine? = null,
    url: Url,
    vararg ignoreDocs: String
): StackTraceAnalyzerBuilder {
    val configure: HttpClientConfig<*>.() -> Unit = {
        val json = kotlinx.serialization.json.Json {
            serializersModule = DocumentedElementModule
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
    }

    val httpClient = if (httpClientEngine == null) {
        HttpClient(configure)
    } else {
        HttpClient(httpClientEngine, configure)
    }

    javaDocCache =
        DocDexJavadocCache(
            httpClient,
            url,
            ignoreDocs.toList(),
            DelegatingCoroutineScope(coroutineDispatcher)
        )

    return this
}

private class DelegatingCoroutineScope(override val coroutineContext: CoroutineContext) :
    CoroutineScope
