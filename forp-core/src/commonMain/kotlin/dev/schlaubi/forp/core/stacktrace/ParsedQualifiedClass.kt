package dev.schlaubi.forp.core.stacktrace

import dev.schlaubi.forp.core.ParsedElement
import dev.schlaubi.forp.core.annotation.ForpInternals
import dev.schlaubi.forp.core.parser.StackTraceParser
import dev.schlaubi.forp.parser.stacktrace.QualifiedClass

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
public data class ParsedQualifiedClass(
    @ForpInternals
    override val context: QualifiedClassContext,
    override val packagePath: String?,
    override val className: String,
    override val innerClasses: List<String>
) : QualifiedClass, ParsedElement {

    /**
     * The combined name of the inner class hierarchy.
     */
    override val innerClassName: String by lazy { innerClasses.joinToString(".") }
}
