package dev.schlaubi.forp.core.stacktrace

import dev.schlaubi.forp.core.InvalidParsedElement
import dev.schlaubi.forp.core.ParsedElement
import dev.schlaubi.forp.core.annotation.ForpInternals
import dev.schlaubi.forp.core.parser.StackTraceParser
import dev.schlaubi.forp.parser.stacktrace.*

/**
 * Low-level parser context for Qualified methods.
 */
@ForpInternals
public typealias QualifiedMethodContext = StackTraceParser.QualifiedMethodContext

/**
 * Representation of any method.
 *
 * @property class the [ParsedQualifiedClass] owning this method
 */
public interface ParsedQualifiedMethod : QualifiedMethod, ParsedElement {
    @ForpInternals
    override val context: QualifiedMethodContext

    override val `class`: ParsedQualifiedClass

    /**
     * Alias to [class] to avoid back ticks.
     */
    override val clazz: ParsedQualifiedClass
        get() = `class`
}

/**
 * Representation of an wrongly formatted method.
 */
public class ParsedInvalidQualifiedMethod(
    @ForpInternals override val context: QualifiedMethodContext,
    override val `class`: ParsedQualifiedClass
) : InvalidQualifiedMethod, ParsedQualifiedMethod, InvalidParsedElement

/**
 * Implementation of [ParsedQualifiedMethod] that represents an actual method call.
 * @property methodName the name of the method call
 */
public data class ParsedDefaultQualifiedMethod(
    @ForpInternals override val context: QualifiedMethodContext,
    override val `class`: ParsedQualifiedClass,
    override val methodName: String
) : DefaultQualifiedMethod, ParsedQualifiedMethod

/**
 * Implementation of [ParsedQualifiedClass] that represents a constructor call (`<init>` in stack trace).
 */
public data class ParsedQualifiedConstructor(
    @ForpInternals override val context: QualifiedMethodContext,
    override val `class`: ParsedQualifiedClass
) : QualifiedConstructor, ParsedQualifiedMethod

/**
 * Implementation of [ParsedQualifiedMethod] that represents a lambda call (`lambda$0` in stack trace)
 */
public data class ParsedLambda(
    @ForpInternals override val context: QualifiedMethodContext,
    override val `class`: ParsedQualifiedClass,
    public override val name: String
) : Lambda, ParsedQualifiedMethod
