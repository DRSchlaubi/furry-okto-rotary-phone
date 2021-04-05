package dev.schlaubi.forp.analyze.javadoc

import dev.schlaubi.forp.parser.stacktrace.QualifiedClass

public interface JavaDocCache {
    public val storedPackage: Set<String>

    public fun prepare()

    public suspend fun findDoc(identifier: QualifiedClass): DocumentedObject?
}
