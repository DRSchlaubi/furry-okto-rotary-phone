package dev.schlaubi.forp.core.stacktrace

import dev.schlaubi.forp.core.ParsedElement
import dev.schlaubi.forp.core.annotation.ForpInternals
import dev.schlaubi.forp.core.parser.StackTraceParser
import dev.schlaubi.forp.parser.stacktrace.CausedStackTrace
import dev.schlaubi.forp.parser.stacktrace.RootStackTrace
import dev.schlaubi.forp.parser.stacktrace.StackTrace
import kotlin.jvm.JvmSynthetic

/**
 * Low-level parser context for Stack traces.
 */
@ForpInternals
public typealias StackTraceContext = StackTraceParser.StackTraceContext

/**
 * Implementation of [ParsedStackTrace] which delegates to a reference.
 * Normally used by [ParsedCausedStackTrace.parent]
 *
 * @see ParsedStackTrace
 */
public class StackTracePointer : ParsedStackTrace {
    private lateinit var reference: ParsedStackTrace

    /**
     * Converts this pointer to a [ParsedStackTrace].
     * @throws IllegalStateException if the reference has not been injected yet
     */
    public fun toStackTrace(): ParsedStackTrace = accessReference()

    /**
     * @throws IllegalStateException if the reference has not been injected yet
     */
    @ForpInternals
    override val context: StackTraceContext
        get() = accessReference().context

    /**
     * @throws IllegalStateException if the reference has not been injected yet
     */
    override val exception: ParsedQualifiedClass
        get() = accessReference().exception

    /**
     * @throws IllegalStateException if the reference has not been injected yet
     */
    override val message: String?
        get() = accessReference().message

    /**
     * @throws IllegalStateException if the reference has not been injected yet
     */
    override val text: String
        get() = accessReference().text

    /**
     * @throws IllegalStateException if the reference has not been injected yet
     */
    override val elements: List<ParsedStackTraceElement>
        get() = accessReference().elements

    /**
     * This is an internal method and not supposed to be called.
     */
    @JvmSynthetic
    internal fun injectReference(reference: ParsedStackTrace) {
        this.reference = reference
    }

    private fun accessReference(): ParsedStackTrace {
        require(this::reference.isInitialized) { "Pointer hasn't received reference yet" }
        return reference
    }
}

/**
 * Interface representing a stack trace.
 *
 * @property exception the [ParsedQualifiedClass] which is the Throwable that got thrown
 * @property message the message of the Exception or `null` if there was none
 * @property elements a [List] of [ParsedStackTraceElement] of this stack trace
 */
public interface ParsedStackTrace : StackTrace, ParsedElement {
    @ForpInternals
    override val context: StackTraceContext
    public override val exception: ParsedQualifiedClass
    public override val message: String?
    public override val elements: List<ParsedStackTraceElement>
}

/**
 * Representation of a child stack trace.
 *
 * @property parent the [ParsedStackTrace] which was caused by this exception
 * @see ParsedStackTrace
 */
public data class ParsedCausedStackTrace(
    override val text: String,
    @ForpInternals override val context: StackTraceContext,
    override val exception: ParsedQualifiedClass,
    override val message: String?,
    override val elements: List<ParsedStackTraceElement>,
    override val parent: StackTracePointer
) : CausedStackTrace, ParsedStackTrace

/**
 * The Root stack trace (Top stack trace of tree without parent).
 *
 * @see ParsedStackTrace
 */
public data class ParsedRootStackTrace(
    override val text: String,
    @ForpInternals override val context: StackTraceContext,
    override val exception: ParsedQualifiedClass,
    override val message: String?,
    override val elements: List<ParsedStackTraceElement>,
    public override val children: List<ParsedCausedStackTrace>
) : RootStackTrace, ParsedStackTrace
