package dev.schlaubi.forp.core.stacktrace

import dev.schlaubi.forp.core.ParsedElement
import dev.schlaubi.forp.core.annotation.ForpInternals
import dev.schlaubi.forp.core.parser.StackTraceParser

/**
 * Low-level parser context for qualified classes.
 */
@ForpInternals
public typealias QualifiedClassContext = StackTraceParser.QualifiedClassContext

/**
 * Representation of a qualified class
 *
 * @property packagePath the package name this class is in
 * @property className the name of the base class
 * @property innerClasses a list of inner classes from bottom to top
 */
public data class QualifiedClass(
    @ForpInternals
    override val context: QualifiedClassContext,
    val packagePath: String?,
    val className: String,
    val innerClasses: List<String>
) : ParsedElement {

    /**
     * Whether this class is an inner class or not
     */
    public val isInnerClass: Boolean
        get() = innerClasses.isNotEmpty()

    /**
     * The combined name of the inner class hierarchy.
     */
    public val innerClassName: String by lazy { innerClasses.joinToString(".") }
}