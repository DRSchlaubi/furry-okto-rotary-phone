package dev.schlaubi.forp.analyze.javadoc

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

internal class DocDex(
    private val url: Url,
    private val httpClient: HttpClient
) {

    /**
     * Retrieves a list of all available javadocs.
     */
    @Suppress("MagicNumber")
    suspend fun allJavadocs(): List<JavaDoc> = httpClient.get(url) {
        url {
            path("javadocs")
        }
    }

    /**
     * Retrieves a list of [DocumentedObjects][DocumentedObject] from the [javadoc].
     */
    suspend fun search(javadoc: String, query: String): List<DocumentedElement> =
        httpClient.get(url) {
            url {
                path("index")
                parameter("javadoc", javadoc)
                parameter("query", query)
            }
        }
}

/**
 * Representation of an indexed javadoc.
 *
 * @property names a list of names which can be used in [DocDex.search]
 * @property link the link to the internal cache version
 * @property actualLink the link to the external version
 */
@Suppress("MemberVisibilityCanBePrivate")
@Serializable
internal class JavaDoc(
    public val names: List<String>,
    public val link: String,
    @SerialName("actual_link")
    public val actualLink: String
)