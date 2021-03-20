package dev.schlaubi.forp.core.stacktrace

import dev.schlaubi.forp.core.ParsedElement
import dev.schlaubi.forp.core.annotation.ForpInternals
import dev.schlaubi.forp.core.parser.StackTraceParser

/**
 * Low-level parser context for Qualified methods.
 */
@ForpInternals
public typealias QualifiedMethodContext = StackTraceParser.QualifiedMethodContext

/**
 * Representation of any method.
 *
 * @property class the [QualifiedClass] owning this method
 */
public interface QualifiedMethod : ParsedElement {
    @ForpInternals
    override val context: QualifiedMethodContext

    public val `class`: QualifiedClass

    /**
     * Alias to [class] to avoid back ticks.
     */
    public val clazz: QualifiedClass
        get() = `class`
}

/**
 * Representation of an wrongly formatted method.
 */
public class InvalidQualifiedMethod(
    @ForpInternals override val context: QualifiedMethodContext,
    override val `class`: QualifiedClass
) : QualifiedMethod

/**
 * Implementation of [QualifiedMethod] that represents an actual method call.
 * @property methodName the name of the method call
 */
public data class DefaultQualifiedMethod(
    @ForpInternals override val context: QualifiedMethodContext,
    override val `class`: QualifiedClass,
    val methodName: String
) : QualifiedMethod

/**
 * Implementation of [QualifiedClass] that represents a constructor call (`<init>` in stack trace).
 */
public data class QualifiedConstructor(
    @ForpInternals override val context: QualifiedMethodContext,
    override val `class`: QualifiedClass
) : QualifiedMethod

/**
 * Implementation of [QualifiedMethod] that represents a lambda call (`lambda$0` in stack trace)
 */
public data class Lambda(
    @ForpInternals override val context: QualifiedMethodContext,
    override val `class`: QualifiedClass,
    public val name: String
) : QualifiedMethod
