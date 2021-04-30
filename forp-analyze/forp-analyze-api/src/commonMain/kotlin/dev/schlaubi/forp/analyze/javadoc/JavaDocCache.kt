package dev.schlaubi.forp.analyze.javadoc

import dev.schlaubi.forp.parser.stacktrace.QualifiedClass
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Abstraction of a Javadoc cache for exception documentation.
 */
public interface JavaDocCache {
    /**
     * A [Set] of packages which are available.
     */
    public val storedPackage: Set<String>

    /**
     * Function called after initialization to queue index building.
     */
    public fun prepare()

    /**
     * Searches for a doc documenting [identifier].
     *
     * @see DocumentedObject
     */
    public suspend fun findDoc(identifier: QualifiedClass): DocumentedObject?

    /**
     * Adds a listener on when [prepare] finshes.
     */
    public fun onReady(block: () -> Unit)
}

/**
 * Suspends until the [JavaDocCache.onReady] listener gets called.
 */
public suspend fun JavaDocCache.awaitReady(): Unit = suspendCoroutine { cont ->
    onReady { cont.resume(Unit) }
}
