package dev.schlaubi.forp.analyze.javadoc

public sealed class DocumentedReference(
    public val raw: String,
    public val `package`: String,
    public val className: String,
) {

    public class Class(raw: String, `package`: String, className: String) : DocumentedReference(
        raw, `package`,
        className
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Class) return false
            return true
        }

        override fun hashCode(): Int {
            return this::class.hashCode()
        }
    }

    public class Field(
        raw: String,
        `package`: String,
        className: String,
        public val fieldName: String,
    ) :
        DocumentedReference(
            raw, `package`,
            className
        ) {


        override fun toString(): String {
            return "DocumentedReference.Field(raw='$raw', `package`='$`package`', className='$className', fieldName='$fieldName')"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Field) return false

            if (fieldName != other.fieldName) return false

            return true
        }

        override fun hashCode(): Int {
            return fieldName.hashCode()
        }
    }

    public class Method(
        raw: String,
        `package`: String,
        className: String,
        public val methodName: String,
    ) :
        DocumentedReference(
            raw, `package`,
            className
        ) {


        override fun toString(): String {
            return "DocumentedReference.Method(raw='$raw', `package`='$`package`', className='$className', methodName='$methodName')"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Method) return false

            if (methodName != other.methodName) return false

            return true
        }

        override fun hashCode(): Int {
            return methodName.hashCode()
        }
    }

    override fun toString(): String {
        return "DocumentedReference(raw='$raw', `package`='$`package`', className='$className')"
    }
}
