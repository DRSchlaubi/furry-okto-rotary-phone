package dev.schlaubi.forp.parser.stacktrace

/**
 * Representation of a qualified class
 *
 * @property packagePath the package name this class is in
 * @property className the name of the base class
 * @property innerClasses a list of inner classes from bottom to top
 */
public interface QualifiedClass {

    public val packagePath: String?
    public val className: String
    public val innerClasses: List<String>

    /**
     * Whether this class is an inner class or not
     */
    public val isInnerClass: Boolean
        get() = innerClasses.isNotEmpty()

    /**
     * The combined name of the inner class hierarchy or `null` if there is none.
     */
    public val innerClassName: String?
}
