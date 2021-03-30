package dev.schlaubi.forp.parser.stacktrace


/**
 * Interface representing a stack trace.
 *
 * @property exception the [QualifiedClass] which is the Throwable that got thrown
 * @property message the message of the Exception or `null` if there was none
 * @property elements a [List] of [StackTraceElement] of this stack trace
 */
public interface StackTrace {
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
public interface CausedStackTrace : StackTrace {
    public val parent: StackTrace
}


/**
 * The Root stack trace (Top stack trace of tree without parent).
 *
 * @see StackTrace
 */
public interface RootStackTrace : StackTrace {
    public val children: List<CausedStackTrace>
}
