package dev.schlaubi.forp.analyze.javadoc

import dev.schlaubi.forp.parser.stacktrace.QualifiedClass
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

public interface JavaDocCache {
    public val storedPackage: Set<String>

    public fun prepare()

    public suspend fun findDoc(identifier: QualifiedClass): DocumentedObject?

    public fun onReady(block: () -> Unit)
}

public suspend fun JavaDocCache.awaitReady(): Unit = suspendCoroutine<Unit> { cont ->
    onReady { cont.resume(Unit) }
}
