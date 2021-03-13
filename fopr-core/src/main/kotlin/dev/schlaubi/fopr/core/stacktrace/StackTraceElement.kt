package dev.schlaubi.fopr.core.stacktrace

import dev.schlaubi.fopr.core.ParsedElement
import dev.schlaubi.fopr.core.annotation.FoprInternals
import dev.schlaubi.fopr.core.parser.StackTraceParser

public typealias StackTraceLineContext = StackTraceParser.StackTraceLineContext

public typealias MethodFileDefinitionContext = StackTraceParser.MethodFileDefinitionContext

public typealias MethodFileSourceContext = StackTraceParser.MethodFileSourceContext

public interface StackTraceElement : ParsedElement {
    @FoprInternals
    override val context: StackTraceLineContext
}

public data class DefaultStackTraceElement(
    @FoprInternals override val context: StackTraceLineContext,
    public val method: QualifiedMethod,
    public val source: Source,
    public val file: SourceFile?,
) : StackTraceElement {

    public interface Source : ParsedElement {
        @FoprInternals
        override val context: MethodFileDefinitionContext
    }

    public data class NativeSource(@FoprInternals override val context: MethodFileDefinitionContext) :
        Source

    public data class UnknownSource(@FoprInternals override val context: MethodFileDefinitionContext) :
        Source

    public data class FileSource(
        @FoprInternals override val context: MethodFileDefinitionContext,
        public val fileName: String,
        public val lineNumber: Int
    ) : Source

    public data class SourceFile(
        @FoprInternals override val context: MethodFileSourceContext,
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
    @FoprInternals override val context: StackTraceLineContext,
    public val skipped: Int,
) : StackTraceElement
