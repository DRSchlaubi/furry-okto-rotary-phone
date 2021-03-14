package dev.schlaubi.forp.core.stacktrace

import dev.schlaubi.forp.core.ParsedElement
import dev.schlaubi.forp.core.annotation.ForpInternals
import dev.schlaubi.forp.core.parser.StackTraceParser

public typealias StackTraceLineContext = StackTraceParser.StackTraceLineContext

public typealias MethodFileDefinitionContext = StackTraceParser.MethodFileDefinitionContext

public typealias MethodFileSourceContext = StackTraceParser.MethodFileSourceContext

public interface StackTraceElement : ParsedElement {
    @ForpInternals
    override val context: StackTraceLineContext
}

public data class DefaultStackTraceElement(
    @ForpInternals override val context: StackTraceLineContext,
    public val method: QualifiedMethod,
    public val source: Source,
    public val file: SourceFile?,
) : StackTraceElement {

    public interface Source : ParsedElement {
        @ForpInternals
        override val context: MethodFileDefinitionContext
    }

    public data class NativeSource(@ForpInternals override val context: MethodFileDefinitionContext) :
        Source

    public data class UnknownSource(@ForpInternals override val context: MethodFileDefinitionContext) :
        Source

    public data class FileSource(
        @ForpInternals override val context: MethodFileDefinitionContext,
        public val fileName: String,
        public val lineNumber: Int
    ) : Source

    public data class SourceFile(
        @ForpInternals override val context: MethodFileSourceContext,
        public val file: String,
        public val version: String
    ) : ParsedElement {
        public val knowsFile: Boolean
            get() = file != "?"
        public val fileIsApproximation: Boolean
            get() = context.TILDE() != null
    }
}

public data class EllipsisStackTraceElement(
    @ForpInternals override val context: StackTraceLineContext,
    public val skipped: Int,
) : StackTraceElement
