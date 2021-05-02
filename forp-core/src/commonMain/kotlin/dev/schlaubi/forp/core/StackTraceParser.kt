package dev.schlaubi.forp.core

import dev.schlaubi.forp.core.internal.charStreamFromCharSequence
import dev.schlaubi.forp.core.parser.StackTraceLexer
import dev.schlaubi.forp.core.stacktrace.*
import org.antlr.v4.kotlinruntime.BufferedTokenStream
import org.antlr.v4.kotlinruntime.CharStream
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.misc.Interval
import kotlin.jvm.JvmStatic
import dev.schlaubi.forp.core.parser.StackTraceParser as LowLevelParser

/**
 * Parser for JVM stack traces.
 *
 * @see StackTraceParser.parse
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
public object StackTraceParser {

    /**
     * Parses the string [input] as a [ParsedRootStackTrace].
     */
    @JvmStatic
    public fun parse(input: String): ParsedRootStackTrace = parse(CharStreams.fromString(input))

    /**
     * Parses the string [input] as a [ParsedRootStackTrace].
     */
    @JvmStatic
    public fun parse(input: CharSequence): ParsedRootStackTrace =
        parse(charStreamFromCharSequence(input))

    /**
     * Parses [input] into a [ParsedRootStackTrace].
     */
    @JvmStatic
    public fun parse(input: CharStream): ParsedRootStackTrace {
        val lexer = StackTraceLexer(input)
        val tokens = BufferedTokenStream(lexer)
        val parser = LowLevelParser(tokens)

        return parser.stackTrace().toAPI()
    }
}

private fun LowLevelParser.StackTraceContext.toAPI(): ParsedRootStackTrace {
    val messageLine = findMessageLine().nn()
    val exception = messageLine.findQualifiedClass().nn().toAPI()
    val elements = findStackTraceLine().map { it.toAPI() }

    val root = StackTracePointer()

    fun LowLevelParser.CausedByLineContext.toAPI(parent: StackTracePointer): ParsedCausedStackTrace {
        val trace = findStackTrace().nn()
        val childMessageLine = trace.findMessageLine().nn()
        val childException = childMessageLine.findQualifiedClass().nn().toAPI()
        val childElements = trace.findStackTraceLine().nn().map { it.toAPI() }

        return ParsedCausedStackTrace(
            trace.text,
            trace,
            childException,
            trace.message,
            childElements,
            parent
        )
    }

    val children = mutableListOf<ParsedCausedStackTrace>()
    var parentPointer = root
    for (causedByLineContext in findCausedByLine()) {
        val element = causedByLineContext.toAPI(parentPointer)
        parentPointer = StackTracePointer().apply { injectReference(element) }
        children.add(element)
    }

    return ParsedRootStackTrace(
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

private fun LowLevelParser.StackTraceLineContext.toAPI(): ParsedStackTraceElement {
    fun toEllipsis(ellipsis: LowLevelParser.EllipsisLineContext): ParsedStackTraceElement {
        val skipped = ellipsis.Number().nn().text.toInt()

        return ParsedEllipsisStackTraceElement(this, skipped)
    }

    fun toElement(element: LowLevelParser.AtLineContext): ParsedStackTraceElement {
        val method = element.findQualifiedMethod().nn().toAPI()
        val definition = element.findMethodFileDefinition().nn().toAPI()
        val source = element.findMethodFileSource()?.toAPI()

        return ParsedDefaultStackTraceElement(this, method, definition, source)
    }

    val atLine = findAtLine()
    val ellipsis = findEllipsisLine()

    return when {
        atLine != null -> toElement(atLine)
        ellipsis != null -> toEllipsis(ellipsis)
        else -> ParsedInvalidStackTraceElement(this)
    }
}

private fun LowLevelParser.MethodFileDefinitionContext.toAPI(): ParsedDefaultStackTraceElement.ParsedSource {
    val file = findClassFile()
    fun toSource(sourceFile: LowLevelParser.SourceFileContext): ParsedDefaultStackTraceElement.ParsedSource {
        val fileName = sourceFile.findSourceFileName().nn().text
        val line = sourceFile.findLineNumber().nn().text.toIntOrNull() ?: -1

        return ParsedDefaultStackTraceElement.ParsedFileSource(this, fileName, line)
    }

    fun toNative(): ParsedDefaultStackTraceElement.ParsedSource =
        ParsedDefaultStackTraceElement.ParsedNativeSource(this)

    fun toUnknown(): ParsedDefaultStackTraceElement.ParsedSource =
        ParsedDefaultStackTraceElement.ParsedUnknownSource(this)

    val sourceFile = file?.findSourceFile()

    return when {
        sourceFile != null -> toSource(sourceFile)
        file?.NATIVE_METHOD() != null -> toNative()
        file?.UNKNOWN_SOURCE() != null -> toUnknown()
        else -> ParsedDefaultStackTraceElement.ParsedInvalidSource(this)
    }
}

private fun LowLevelParser.MethodFileSourceContext.toAPI(): ParsedDefaultStackTraceElement.ParsedSourceFile {
    val file = findJarFile().nn().text
    val version = findString().nn().text

    return ParsedDefaultStackTraceElement.ParsedSourceFile(this, file, version)
}

private fun LowLevelParser.QualifiedMethodContext.toAPI(): ParsedQualifiedMethod {
    val clazz = findQualifiedClass().nn().toAPI()

    fun toMethod(name: LowLevelParser.MethodNameContext): ParsedQualifiedMethod {
        return ParsedDefaultQualifiedMethod(this, clazz, name.text)
    }

    fun toConstructor(): ParsedQualifiedMethod {
        return ParsedQualifiedConstructor(this, clazz)
    }

    fun toLambda(lambda: LowLevelParser.LambdaContext): ParsedQualifiedMethod {

        return ParsedLambda(this, clazz, lambda.text)
    }

    val methodName = findMethodName()
    val lambda = findLambda()

    return when {
        methodName != null -> toMethod(methodName)
        findConstructorDef() != null -> toConstructor()
        lambda != null -> toLambda(lambda)
        else -> ParsedInvalidQualifiedMethod(this, clazz)
    }
}

private fun LowLevelParser.QualifiedClassContext.toAPI(): ParsedQualifiedClass {
    val packagePath = findPackagePath()?.findIdentifier()?.joinToString(".") { it.text }
    val className = findClassName().nn().text
    val innerClasses = findInnerClassName().nn().map { it.findClassName().nn().text }

    return ParsedQualifiedClass(this, packagePath, className, innerClasses)
}

private class ImmutableList<T>(val delegate: List<T>) : List<T> by delegate {
    override fun toString(): String = delegate.toString()
}

private fun <T> T?.nn(): T = this ?: error("Could not find needed element")

private val LowLevelParser.StackTraceContext.message: String?
    get() = findMessageLine().nn().findMessage()?.findWhiteSpacedString()?.fullText

private val ParserRuleContext.fullText: String
    get() {
        val start = start
        val stop = stop

        return if (start == null || stop == null || start.startIndex < 0 || stop.stopIndex < 0) {
            text
        } else {
            start.inputStream!!
                .getText(Interval.of(start.startIndex, stop.stopIndex))
        }
    }
