package dev.schlaubi.forp.parser.stacktrace

/**
 * Base class for any kind of StackTraceElement
 */
public interface StackTraceElement

/**
 * Stack trace element which couldn't get parsed properly due to formatting errors.
 */
public interface InvalidStackTraceElement : StackTraceElement

/**
 * A normal stack trace element.
 *
 * @property method the [QualifiedMethod] which was called in the stack
 * @property source the [Source] which contained the method
 * @property file the [SourceFile] which contained the class file
 */
public interface DefaultStackTraceElement : StackTraceElement {
    public val method: QualifiedMethod
    public val source: Source
    public val file: SourceFile?

    /**
     * Base class for all kinds of sources.
     */
    public interface Source

    /**
     * A `Native Method` call in the stack
     */
    public interface NativeSource : Source

    /**
     * A `Unknown Method` call in the stack
     */
    public interface UnknownSource : Source

    /**
     * A Source which could not get parsed properly.
     */
    public interface InvalidSource : Source

    /**
     * A normal file source.
     *
     * @property fileName the name of the file containing the method
     * @property lineNumber the line in the file which caused the exception
     */
    public interface FileSource : Source {
        public val fileName: String
        public val lineNumber: Int
    }

    /**
     * A source file (normally a jar file) in the stack
     *
     * @property file the name of the file
     * @property version a version identifier (e.g. JDK version) of the file
     * @property knowsFile whether the file is unknown or not
     * @property fileIsApproximation whether the file was an approximation
     */
    public interface SourceFile {
        public val file: String
        public val version: String
        public val knowsFile: Boolean
        public val fileIsApproximation: Boolean
    }
}

/**
 * Stack trace element which signalises that [skipped] lines got skipped.
 */
public interface EllipsisStackTraceElement : StackTraceElement {
    public val skipped: Int
}
