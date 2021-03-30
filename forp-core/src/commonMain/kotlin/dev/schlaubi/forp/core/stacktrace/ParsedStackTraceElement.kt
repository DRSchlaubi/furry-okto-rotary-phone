package dev.schlaubi.forp.core.stacktrace

import dev.schlaubi.forp.core.InvalidParsedElement
import dev.schlaubi.forp.core.ParsedElement
import dev.schlaubi.forp.core.annotation.ForpInternals
import dev.schlaubi.forp.core.parser.StackTraceParser
import dev.schlaubi.forp.parser.stacktrace.DefaultStackTraceElement
import dev.schlaubi.forp.parser.stacktrace.EllipsisStackTraceElement
import dev.schlaubi.forp.parser.stacktrace.InvalidStackTraceElement
import dev.schlaubi.forp.parser.stacktrace.StackTraceElement

public typealias StackTraceLineContext = StackTraceParser.StackTraceLineContext

public typealias MethodFileDefinitionContext = StackTraceParser.MethodFileDefinitionContext

public typealias MethodFileSourceContext = StackTraceParser.MethodFileSourceContext

/**
 * Base class for any kind of StackTraceElement
 */
public interface ParsedStackTraceElement : StackTraceElement, ParsedElement {
    @ForpInternals
    override val context: StackTraceLineContext
}

/**
 * Stack trace element which couldn't get parsed properly due to formatting errors.
 */
public data class ParsedInvalidStackTraceElement(@ForpInternals override val context: StackTraceLineContext) :
    InvalidStackTraceElement, ParsedStackTraceElement, InvalidParsedElement

/**
 * A normal stack trace element.
 *
 * @property method the [ParsedQualifiedMethod] which was called in the stack
 * @property source the [ParsedSource] which contained the method
 * @property file the [ParsedSourceFile] which contained the class file
 */
public data class ParsedDefaultStackTraceElement(
    @ForpInternals override val context: StackTraceLineContext,
    public override val method: ParsedQualifiedMethod,
    public override val source: ParsedSource,
    public override val file: ParsedSourceFile?,
) : DefaultStackTraceElement, ParsedStackTraceElement {

    /**
     * Base class for all kinds of sources.
     */
    public interface ParsedSource : DefaultStackTraceElement.Source, ParsedElement {
        @ForpInternals
        override val context: MethodFileDefinitionContext
    }

    /**
     * A `Native Method` call in the stack
     */
    public data class ParsedNativeSource(@ForpInternals override val context: MethodFileDefinitionContext) :
        DefaultStackTraceElement.NativeSource, ParsedSource

    /**
     * A `Unknown Method` call in the stack
     */
    public data class ParsedUnknownSource(@ForpInternals override val context: MethodFileDefinitionContext) :
        DefaultStackTraceElement.UnknownSource, ParsedSource

    /**
     * A Source which could not get parsed properly.
     */
    public data class ParsedInvalidSource(@ForpInternals override val context: MethodFileDefinitionContext) :
        DefaultStackTraceElement.InvalidSource, ParsedSource, InvalidParsedElement

    /**
     * A normal file source.
     *
     * @property fileName the name of the file containing the method
     * @property lineNumber the line in the file which caused the exception
     */
    public data class ParsedFileSource(
        @ForpInternals override val context: MethodFileDefinitionContext,
        public override val fileName: String,
        public override val lineNumber: Int
    ) : DefaultStackTraceElement.FileSource, ParsedSource

    /**
     * A source file (normally a jar file) in the stack
     *
     * @property file the name of the file
     * @property version a version identifier (e.g. JDK version) of the file
     * @property knowsFile whether the file is unknown or not
     * @property fileIsApproximation whether the file was an approximation
     */
    public data class ParsedSourceFile(
        @ForpInternals override val context: MethodFileSourceContext,
        public override val file: String,
        public override val version: String
    ) : DefaultStackTraceElement.SourceFile, ParsedElement {
        public override val knowsFile: Boolean
            get() = file != "?"
        public override val fileIsApproximation: Boolean
            get() = context.TILDE() != null
    }
}

/**
 * Stack trace element which signalises that [skipped] lines got skipped.
 */
public data class ParsedEllipsisStackTraceElement(
    @ForpInternals override val context: StackTraceLineContext,
    public override val skipped: Int,
) : EllipsisStackTraceElement, ParsedStackTraceElement
