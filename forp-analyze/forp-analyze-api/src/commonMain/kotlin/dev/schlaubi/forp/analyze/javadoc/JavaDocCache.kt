package dev.schlaubi.forp.analyze.javadoc

public interface JavaDocCache {
    public val storedPackage: List<String>

    public fun findDoc(identifier: String): Any
}
