package dev.schlaubi.forp.analyze.javadoc

import dev.schlaubi.forp.parser.stacktrace.QualifiedClass
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

// https://regex101.com/r/JvZoYD/4
private val classReferenceRegex = "(?:((?:(?:[a-zA-Z0-9]+)\\.?)+)\\.)?([a-zA-Z0-9]+)".toRegex()

// https://regex101.com/r/26jVyw/7
private val referenceRegex =
    "(?:((?:[a-zA-Z0-9]+\\.?)+)\\.)?([a-zA-Z0-9]+)[#.](<init>|[a-zA-Z0-9(), ]+)".toRegex()

/**
 * Implementation of [JavaDocCache] that uses [DocDex](https://github.com/PiggyPiglet/DocDex).
 *
 * @see useDocDex
 */
public class DocDexJavadocCache(
    private val httpClient: HttpClient,
    docdexUrl: Url,
    private val ignoreList: List<String>,
    private val launcher: CoroutineScope
) : CoroutineScope by launcher, JavaDocCache {

    private val docdex = DocDex(docdexUrl, httpClient)
    private lateinit var packageCache: Map<String, String>
    override val storedPackage: Set<String>
        get() = packageCache.keys


    override fun prepare() {
        populateIndex()
    }

    private fun populateIndex() {
        launch {
            println("Index population start")
            val allJavadocs = docdex.allJavadocs()

            val packageCache = mutableMapOf<String, String>()
            coroutineScope {
                allJavadocs.asSequence()
                    .filter { it.names.all { it !in ignoreList } }
                    .forEach { javadoc ->
                        launch {
                            println("Indexing ${javadoc.names.first()}")
                            val packageIndex = httpClient.get<String>(javadoc.link) {
                                url {
                                    val name = encodedPath.substringBeforeLast('/').drop(1)
                                    path(name, "package-index")
                                }
                            }

                            val supportedPackages = packageIndex.lines()
                            supportedPackages.forEach {
                                val realName = it.trimEnd().removeSuffix("\t")
                                if (realName.isNotBlank() && !packageCache.containsKey(realName)) {
                                    packageCache[realName] = javadoc.names.first()
                                }
                            }

                            println("Indexing of ${javadoc.names.first()} complete")
                        }
                    }
            }

            this@DocDexJavadocCache.packageCache = packageCache.toMap()
        }
    }

    override suspend fun findDoc(identifier: QualifiedClass): DocumentedObject? {
        require(::packageCache.isInitialized) { "Cache hasn't been initialized yet" }

        val reference = Reference(
            identifier.toString(),
            identifier.packagePath,
            identifier.className,
            null
        )

        return packageCache[reference.`package`]?.let {
            docdex.search(
                it,
                reference.toDocDexQuery()
            ).firstOrNull()?.`object`
        }
    }

    /**
     * Reference to a documented Java element (e.g. java.lang.String#substring(int, int)
     *
     * @property package the package of the reference
     * @property clazz the class name of the reference
     * @property method the reference name
     */
    private data class Reference(
        val raw: String,
        val `package`: String?,
        val clazz: String?,
        val method: String?
    ) {

        /**
         * Converts this reference to a searchable DocDex query.
         */
        fun toDocDexQuery(): String {
            val query = StringBuilder()
            if (!`package`.isNullOrBlank()) {
                query.append(`package`).append('.')
            }
            if (!clazz.isNullOrBlank()) {
                query.append(clazz)
            }
            if (!method.isNullOrBlank()) {
                query.append("~").append(method)
            }
            return query.toString()
        }
    }
}
