package dev.schlaubi.fopr.core

import dev.schlaubi.fopr.core.parser.StackTraceLexer
import dev.schlaubi.fopr.core.stacktrace.*
import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.misc.Interval
import java.io.InputStream
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import dev.schlaubi.fopr.core.parser.StackTraceParser as LowLevelParser

/**
 * Parser for JVM stack traces.
 *
 * @see StackTraceParser.parse
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
public object StackTraceParser {

    /**
     * Parses the string [input] as a [RootStackTrace].
     */
    public fun parse(input: String): RootStackTrace = parse(CharStreams.fromString(input))

    /**
     * Parses the content of [channel] as a [RootStackTrace] using [charset].
     *
     * **Important:** [channel] is not going to get closed, you need to close it manually
     */
    public fun parse(
        channel: ReadableByteChannel,
        charset: Charset = Charsets.UTF_8
    ): RootStackTrace =
        parse(CharStreams.fromChannel(channel, charset))

    /**
     * Parses the content of [path] as a [RootStackTrace] using [charset].
     */
    public fun parse(path: Path, charset: Charset = Charsets.UTF_8): RootStackTrace {
        require(Files.isRegularFile(path)) { "File has to be regular file" }
        require(Files.isReadable(path)) { "Unable to read file" }
        return FileChannel.open(path, StandardOpenOption.READ)
            .use { channel -> parse(channel, charset) }
    }

    /**
     * Parses the content of [input] using [charset] into a [RootStackTrace].
     *
     * **Important:** This will call [InputStream.close]
     */
    public fun parse(input: InputStream, charset: Charset = Charsets.UTF_8): RootStackTrace =
        parse(CharStreams.fromStream(input, charset))

    /**
     * Parses [input] into a [RootStackTrace].
     */
    public fun parse(input: CharStream): RootStackTrace {
        val lexer = StackTraceLexer(input)
        val tokens = BufferedTokenStream(lexer)
        val parser = LowLevelParser(tokens)

        return parser.stackTrace().toAPI()
    }
}

private fun LowLevelParser.StackTraceContext.toAPI(): RootStackTrace {
    val messageLine = messageLine()
    val exception = messageLine.qualifiedClass().toAPI()
    val elements = stackTraceLine().map { it.toAPI() }

    val root = StackTracePointer()

    fun LowLevelParser.CausedByLineContext.toAPI(parent: StackTracePointer): CausedStackTrace {
        val trace = stackTrace()
        val childMessageLine = trace.messageLine()
        val childException = childMessageLine.qualifiedClass().toAPI()
        val childElements = trace.stackTraceLine().map { it.toAPI() }

        return CausedStackTrace(
            trace.text,
            trace,
            childException,
            trace.message,
            childElements,
            parent
        )
    }

    val children = mutableListOf<CausedStackTrace>()
    var parentPointer = root
    for (causedByLineContext in causedByLine()) {
        val element = causedByLineContext.toAPI(parentPointer)
        parentPointer = StackTracePointer().apply { injectReference(element) }
        children.add(element)
    }

    return RootStackTrace(
        text,
        this,
        exception,
        message,
        elements,
        ImmutableList(children)
    ).also {
        root.injectReference(it)
    }
}

private fun LowLevelParser.StackTraceLineContext.toAPI(): StackTraceElement {
    fun toEllipsis(): StackTraceElement {
        val ellipsis = ellipsisLine()
        val skipped = ellipsis.Number().text.toInt()

        return EllipsisStackTraceElement(this, skipped)
    }

    fun toElement(): StackTraceElement {
        val element = atLine()
        val method = element.qualifiedMethod().toAPI()
        val definition = element.methodFileDefinition().toAPI()
        val source = element.methodFileSource()?.toAPI()

        return DefaultStackTraceElement(this, method, definition, source)
    }

    return when {
        atLine() != null -> toElement()
        ellipsisLine() != null -> toEllipsis()
        else -> error("Unexpected parsing error")
    }
}

private fun LowLevelParser.MethodFileDefinitionContext.toAPI(): DefaultStackTraceElement.Source {
    val file = classFile()
    fun toSource(): DefaultStackTraceElement.Source {
        val sourceFile = file.sourceFile()
        val fileName = sourceFile.sourceFileName().text
        val line = sourceFile.lineNumber().text.toInt()

        return DefaultStackTraceElement.FileSource(this, fileName, line)
    }

    fun toNative(): DefaultStackTraceElement.Source =
        DefaultStackTraceElement.NativeSource(this)

    fun toUnknown(): DefaultStackTraceElement.Source =
        DefaultStackTraceElement.UnknownSource(this)

    return when {
        file.sourceFile() != null -> toSource()
        file.NATIVE_METHOD() != null -> toNative()
        file.UNKNOWN_SOURCE() != null -> toUnknown()
        else -> error("Unexpected parsing error")
    }
}

private fun LowLevelParser.MethodFileSourceContext.toAPI(): DefaultStackTraceElement.SourceFile {
    val file = jarFile().text
    val version = this.string().text

    return DefaultStackTraceElement.SourceFile(this, file, version)
}

private fun LowLevelParser.QualifiedMethodContext.toAPI(): QualifiedMethod {
    val clazz = qualifiedClass().toAPI()

    fun toMethod(): QualifiedMethod {
        val name = methodName().text

        return DefaultQualifiedMethod(this, clazz, name)
    }

    fun toConstructor(): QualifiedMethod {
        return QualifiedConstructor(this, clazz)
    }

    return when {
        methodName() != null -> toMethod()
        constructor() != null -> toConstructor()
        else -> error("Unexpected parsing error")
    }
}

private fun LowLevelParser.QualifiedClassContext.toAPI(): QualifiedClass {
    val packagePath = packagePath()?.identifier()?.joinToString(".") { it.text }
    val className = className().text
    val innerClasses = innerClassName().map { it.className().text }

    return QualifiedClass(this, packagePath, className, innerClasses)
}

private class ImmutableList<T>(val delegate: List<T>) : List<T> by delegate {
    override fun toString(): String = delegate.toString()
}

private val LowLevelParser.StackTraceContext.message: String?
    get() = messageLine().message()?.whiteSpacedString()?.fullText

private val ParserRuleContext.fullText: String
    get() = if (start == null || stop == null || start.startIndex < 0 || stop.stopIndex < 0) text else start.inputStream
        .getText(Interval.of(start.startIndex, stop.stopIndex))
