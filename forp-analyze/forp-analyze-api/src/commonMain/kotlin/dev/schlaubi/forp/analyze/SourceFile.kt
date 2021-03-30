package dev.schlaubi.forp.analyze

/**
 * Representation of a file which possible caused on of the stacktrace.
 */
public interface SourceFile {

    /**
     * The name of the file.
     */
    public val name: String

    /**
     * The plain content of the file.
     */
    public val content: String

    /**
     * Whether this file is potentially just a snippet of the actual file.
     */
    public val potentiallyIncomplete: Boolean

    /**
     * The line at [lineNumber] in the file.
     */
    public fun lineAt(lineNumber: Int): String
}
