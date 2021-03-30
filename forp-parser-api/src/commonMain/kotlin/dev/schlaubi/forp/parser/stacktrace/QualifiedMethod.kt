package dev.schlaubi.forp.parser.stacktrace


/**
 * Representation of any method.
 *
 * @property class the [QualifiedClass] owning this method
 */
public interface QualifiedMethod {

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
public interface InvalidQualifiedMethod : QualifiedMethod

/**
 * Implementation of [QualifiedMethod] that represents an actual method call.
 * @property methodName the name of the method call
 */
public interface DefaultQualifiedMethod : QualifiedMethod {
    public val methodName: String
}

/**
 * Representation of an wrongly formatted method.
 */
public interface QualifiedConstructor : QualifiedMethod


/**
 * Implementation of [ParsedQualifiedMethod] that represents a lambda call (`lambda$0` in stack trace)
 */
public interface Lambda : QualifiedMethod {
    public val name: String
}
