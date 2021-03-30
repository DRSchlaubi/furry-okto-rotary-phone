package dev.schlaubi.forp.analyze.javadoc

public sealed class DocumentedReference(
    public val raw: String,
    public val `package`: String,
    public val className: String
) {

    public class Class(raw: String, `package`: String, className: String) : DocumentedReference(
        raw, `package`,
        className
    )

    public class Field(
        raw: String,
        `package`: String,
        className: String,
        public val fieldName: String
    ) :
        DocumentedReference(
            raw, `package`,
            className
        ) {

        override fun toString(): String {
            return "DocumentedReference.Field(raw='$raw', `package`='$`package`', className='$className', fieldName='$fieldName')"
        }
    }

    public class Method(
        raw: String,
        `package`: String,
        className: String,
        public val methodName: String
    ) :
        DocumentedReference(
            raw, `package`,
            className
        ) {
        override fun toString(): String {
            return "DocumentedReference.Method(raw='$raw', `package`='$`package`', className='$className', methodName='$methodName')"
        }
    }

    override fun toString(): String {
        return "DocumentedReference(raw='$raw', `package`='$`package`', className='$className')"
    }
}
