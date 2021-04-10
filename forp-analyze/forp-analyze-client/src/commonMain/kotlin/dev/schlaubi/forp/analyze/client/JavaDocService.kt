package dev.schlaubi.forp.analyze.client

import dev.schlaubi.forp.analyze.javadoc.DocumentedObject
import dev.schlaubi.forp.analyze.javadoc.JavaDocCache
import dev.schlaubi.forp.parser.stacktrace.QualifiedClass
import io.ktor.client.request.*
import kotlinx.coroutines.launch

internal class RemoteJavaDocCache(private val restResources: RestResources) : JavaDocCache {
    override lateinit var storedPackage: Set<String>

    override fun prepare() {
        restResources.launch {
            val packs: Set<String> = restResources.httpClient.get(restResources.url) {
                url {
                    path("packages")
                }
            }

            storedPackage = packs
        }
    }

    override suspend fun findDoc(identifier: QualifiedClass): DocumentedObject? =
        restResources.httpClient.get(restResources.url) {
            url {
                parameters.append("query", "${identifier.packagePath}#${identifier.className}")
            }
        }
}
