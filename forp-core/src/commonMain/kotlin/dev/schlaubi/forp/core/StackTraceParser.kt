package dev.schlaubi.forp.core

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
     * Parses the string [input] as a [RootStackTrace].
     */
    @JvmStatic
    public fun parse(input: String): RootStackTrace = parse(CharStreams.fromString(input))

    /**
     * Parses [input] into a [RootStackTrace].
     */
    @JvmStatic
    public fun parse(input: CharStream): RootStackTrace {
        val lexer = StackTraceLexer(input)
        val tokens = BufferedTokenStream(lexer)
        val parser = LowLevelParser(tokens)

        return parser.stackTrace().toAPI()
    }
}

private fun LowLevelParser.StackTraceContext.toAPI(): RootStackTrace {
    val messageLine = findMessageLine().nn()
    val exception = messageLine.findQualifiedClass().nn().toAPI()
    val elements = findStackTraceLine().map { it.toAPI() }

    val root = StackTracePointer()

    fun LowLevelParser.CausedByLineContext.toAPI(parent: StackTracePointer): CausedStackTrace {
        val trace = findStackTrace().nn()
        val childMessageLine = trace.findMessageLine().nn()
        val childException = childMessageLine.findQualifiedClass().nn().toAPI()
        val childElements = trace.findStackTraceLine().nn().map { it.toAPI() }

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
    for (causedByLineContext in findCausedByLine()) {
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
    fun toEllipsis(ellipsis: LowLevelParser.EllipsisLineContext): StackTraceElement {
        val skipped = ellipsis.Number().nn().text.toInt()

        return EllipsisStackTraceElement(this, skipped)
    }

    fun toElement(element: LowLevelParser.AtLineContext): StackTraceElement {
        val method = element.findQualifiedMethod().nn().toAPI()
        val definition = element.findMethodFileDefinition().nn().toAPI()
        val source = element.findMethodFileSource()?.toAPI()

        return DefaultStackTraceElement(this, method, definition, source)
    }

    val atLine = findAtLine()
    val ellipsis = findEllipsisLine()

    return when {
        atLine != null -> toElement(atLine)
        ellipsis != null -> toEllipsis(ellipsis)
        else -> InvalidStackTraceElement(this)
    }
}

private fun LowLevelParser.MethodFileDefinitionContext.toAPI(): DefaultStackTraceElement.Source {
    val file = findClassFile()
    fun toSource(sourceFile: LowLevelParser.SourceFileContext): DefaultStackTraceElement.Source {
        val fileName = sourceFile.findSourceFileName().nn().text
        val line = sourceFile.findLineNumber().nn().text.toInt()

        return DefaultStackTraceElement.FileSource(this, fileName, line)
    }

    fun toNative(): DefaultStackTraceElement.Source =
        DefaultStackTraceElement.NativeSource(this)

    fun toUnknown(): DefaultStackTraceElement.Source =
        DefaultStackTraceElement.UnknownSource(this)

    val sourceFile = file?.findSourceFile()

    return when {
        sourceFile != null -> toSource(sourceFile)
        file?.NATIVE_METHOD() != null -> toNative()
        file?.UNKNOWN_SOURCE() != null -> toUnknown()
        else -> DefaultStackTraceElement.InvalidSource(this)
    }
}

private fun LowLevelParser.MethodFileSourceContext.toAPI(): DefaultStackTraceElement.SourceFile {
    val file = findJarFile().nn().text
    val version = findString().nn().text

    return DefaultStackTraceElement.SourceFile(this, file, version)
}

private fun LowLevelParser.QualifiedMethodContext.toAPI(): QualifiedMethod {
    val clazz = findQualifiedClass().nn().toAPI()

    fun toMethod(name: LowLevelParser.MethodNameContext): QualifiedMethod {
        return DefaultQualifiedMethod(this, clazz, name.text)
    }

    fun toConstructor(): QualifiedMethod {
        return QualifiedConstructor(this, clazz)
    }

    fun toLambda(lambda: LowLevelParser.LambdaContext): QualifiedMethod {

        return Lambda(this, clazz, lambda.text)
    }

    val methodName = findMethodName()
    val lambda = findLambda()

    return when {
        methodName != null -> toMethod(methodName)
        findConstructorDef() != null -> toConstructor()
        lambda != null -> toLambda(lambda)
        else -> InvalidQualifiedMethod(this, clazz)
    }
}

private fun LowLevelParser.QualifiedClassContext.toAPI(): QualifiedClass {
    val packagePath = findPackagePath()?.findIdentifier()?.joinToString(".") { it.text }
    val className = findClassName().nn().text
    val innerClasses = findInnerClassName().nn().map { it.findClassName().nn().text }

    return QualifiedClass(this, packagePath, className, innerClasses)
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
