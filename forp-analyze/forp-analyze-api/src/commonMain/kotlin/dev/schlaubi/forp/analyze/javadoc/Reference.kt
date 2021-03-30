package dev.schlaubi.forp.analyze.javadoc

/**
 * Reference to a documented Java element (e.g. java.lang.String#substring(int, int)
 *
 * @property raw the raw String of the reference
 * @property package the package of the reference
 * @property clazz the class name of the reference
 * @property method the reference name
 */
public interface Reference {
    public val raw: String
    public val `package`: String
    public val clazz: String?
    public val method: String?
}
