package dev.schlaubi.fopr.core.stacktrace

import dev.schlaubi.fopr.core.ParsedElement
import dev.schlaubi.fopr.core.annotation.FoprInternals
import dev.schlaubi.fopr.core.parser.StackTraceParser

/**
 * Low-level parser context for Qualified methods.
 */
@FoprInternals
public typealias QualifiedMethodContext = StackTraceParser.QualifiedMethodContext

/**
 * Representation of any method.
 *
 * @property class the [QualifiedClass] owning this method
 */
public interface QualifiedMethod : ParsedElement {
    @FoprInternals
    override val context: QualifiedMethodContext

    public val `class`: QualifiedClass

    /**
     * Alias to [class] to avoid back ticks.
     */
    public val clazz: QualifiedClass
        get() = `class`
}

/**
 * Implementation of [QualifiedMethod] that represents an actual method call.
 * @property methodName the name of the method call
 */
public data class DefaultQualifiedMethod(
    @FoprInternals override val context: QualifiedMethodContext,
    override val `class`: QualifiedClass,
    val methodName: String
) : QualifiedMethod

/**
 * Implementation of [QualifiedClass] that represents a constructor call (`<init>` in stack trace).
 */
public data class QualifiedConstructor(
    @FoprInternals override val context: QualifiedMethodContext,
    override val `class`: QualifiedClass
) : QualifiedMethod
