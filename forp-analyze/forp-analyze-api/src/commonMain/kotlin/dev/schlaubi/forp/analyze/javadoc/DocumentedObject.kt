package dev.schlaubi.forp.analyze.javadoc

public interface DocumentedObject {

    public val link: String
    public val type: Type

    @Suppress("VariableNaming")
    public val `package`: String
    public val name: String
    public val description: String

    public val strippedDescription: String
    public val annotations: List<DocumentedReference>
    public val deprecated: Boolean

    public val deprecationMessage: String
    public val modifiers: List<String>
    public val metadata: Metadata

    public enum class Type {
        CLASS,
        INTERFACE,
        ENUM,
        ANNOTATION,

        METHOD,
        CONSTRUCTOR,

        UNKNOWN
    }

    public interface Metadata
}

public interface DocumentedClassObject : DocumentedObject {
    override val metadata: ClassMetadata
}

public interface DocumentedClass : DocumentedClassObject

public interface DocumentedEnum : DocumentedClassObject

public interface DocumentedInterface : DocumentedClassObject

public interface DocumentedAnnotation : DocumentedClassObject

public interface DocumentedMethodObject : DocumentedObject {
    override val metadata: MethodMetadata
}

public interface DocumentedMethod : DocumentedMethodObject

public interface DocumentedConstructor : DocumentedMethodObject
