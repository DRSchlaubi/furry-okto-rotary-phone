package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.javadoc.DocumentedObject
import dev.schlaubi.forp.analyze.javadoc.JavaDocCache
import dev.schlaubi.forp.parser.stacktrace.QualifiedClass
import io.ktor.client.request.*
import kotlinx.coroutines.launch

private typealias ReadyConsumer = () -> Unit

internal class RemoteJavaDocCache(private val restResources: RestResources) : JavaDocCache {
    override lateinit var storedPackage: Set<String>
    private val readyListeners: MutableList<ReadyConsumer> = mutableListOf()

    override fun prepare() {
        restResources.launch {
            val packs: Set<String> = restResources.httpClient.get(restResources.url) {
                url {
                    path("javadocs", "packages")
                }
            }

            storedPackage = packs
            readyListeners.forEach(ReadyConsumer::invoke)
        }
    }

    override suspend fun findDoc(identifier: QualifiedClass): DocumentedObject? =
        restResources.httpClient.get(restResources.url) {
            url {
                path("javadocs")
                parameters.append("query", "${identifier.packagePath}#${identifier.className}")
            }
        }

    override fun onReady(block: () -> Unit) {
        readyListeners += block
    }
}
