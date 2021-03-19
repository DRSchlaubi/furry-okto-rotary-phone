package dev.schlaubi.forp.core.stacktrace

import dev.schlaubi.forp.core.ParsedElement
import dev.schlaubi.forp.core.annotation.ForpInternals
import dev.schlaubi.forp.core.parser.StackTraceParser
import kotlin.jvm.JvmSynthetic

/**
 * Low-level parser context for Stack traces.
 */
@ForpInternals
public typealias StackTraceContext = StackTraceParser.StackTraceContext

/**
 * Implementation of [StackTrace] which delegates to a reference.
 * Normally used by [CausedStackTrace.parent]
 *
 * @see StackTrace
 */
public class StackTracePointer : StackTrace {
    private lateinit var reference: StackTrace

    /**
     * Converts this pointer to a [StackTrace].
     * @throws IllegalStateException if the reference has not been injected yet
     */
    public fun toStackTrace(): StackTrace = accessReference()

    /**
     * @throws IllegalStateException if the reference has not been injected yet
     */
    @ForpInternals
    override val context: StackTraceContext
        get() = accessReference().context

    /**
     * @throws IllegalStateException if the reference has not been injected yet
     */
    override val exception: QualifiedClass
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
    override val elements: List<StackTraceElement>
        get() = accessReference().elements

    /**
     * This is an internal method and not supposed to be called.
     */
    @JvmSynthetic
    internal fun injectReference(reference: StackTrace) {
        this.reference = reference
    }

    private fun accessReference(): StackTrace {
        require(this::reference.isInitialized) { "Pointer hasn't received reference yet" }
        return reference
    }
}

/**
 * Interface representing a stack trace.
 *
 * @property exception the [QualifiedClass] which is the Throwable that got thrown
 * @property message the message of the Exception or `null` if there was none
 * @property elements a [List] of [StackTraceElement] of this stack trace
 */
public interface StackTrace : ParsedElement {
    @ForpInternals
    override val context: StackTraceContext
    public val exception: QualifiedClass
    public val message: String?
    public val elements: List<StackTraceElement>
}

/**
 * Representation of a child stack trace.
 *
 * @property parent the [StackTrace] which was caused by this exception
 * @see StackTrace
 */
public data class CausedStackTrace(
    override val text: String,
    @ForpInternals override val context: StackTraceContext,
    override val exception: QualifiedClass,
    override val message: String?,
    override val elements: List<StackTraceElement>,
    val parent: StackTracePointer
) : StackTrace

/**
 * The Root stack trace (Top stack trace of tree without parent).
 *
 * @see StackTrace
 */
public data class RootStackTrace(
    override val text: String,
    @ForpInternals override val context: StackTraceContext,
    override val exception: QualifiedClass,
    override val message: String?,
    override val elements: List<StackTraceElement>,
    public val children: List<CausedStackTrace>
) : StackTrace
