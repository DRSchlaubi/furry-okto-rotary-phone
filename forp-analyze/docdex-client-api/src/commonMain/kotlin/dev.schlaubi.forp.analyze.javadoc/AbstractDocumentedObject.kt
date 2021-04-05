package dev.schlaubi.forp.analyze.javadoc

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.modules.polymorphic

val DocumentedElementModule: SerializersModule = SerializersModule {
    contextual(DocumentedReferenceSerializer)

    polymorphic(AbstractDocumentedObject::class) {
        subclass(
            AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedClassImpl::class,
            AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedClassImpl.serializer()
        )
        subclass(
            AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedEnumImpl::class,
            AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedEnumImpl.serializer()
        )
        subclass(
            AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedInterfaceImpl::class,
            AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedInterfaceImpl.serializer()
        )
        subclass(
            AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedAnnotationImpl::class,
            AbstractDocumentedObject.AbstractDocumentedClassObject.DocumentedAnnotationImpl.serializer()
        )
        subclass(
            AbstractDocumentedObject.AbstractDocumentedMethodObject.DocumentedMethodImpl::class,
            AbstractDocumentedObject.AbstractDocumentedMethodObject.DocumentedMethodImpl.serializer()
        )
        subclass(
            AbstractDocumentedObject.AbstractDocumentedMethodObject.DocumentedConstructorImpl::class,
            AbstractDocumentedObject.AbstractDocumentedMethodObject.DocumentedConstructorImpl.serializer()
        )
    }
}

@Suppress("unused")
@Serializable
sealed class AbstractDocumentedObject : DocumentedObject {
    override val type: DocumentedObject.Type
        get() = when (this) {
            is DocumentedClass -> DocumentedObject.Type.CLASS
            is DocumentedEnum -> DocumentedObject.Type.ENUM
            is DocumentedInterface -> DocumentedObject.Type.INTERFACE
            is DocumentedAnnotation -> DocumentedObject.Type.ANNOTATION
            is DocumentedConstructor -> DocumentedObject.Type.CONSTRUCTOR
            is DocumentedMethod -> DocumentedObject.Type.METHOD
            else -> error("Unknown type")
        }

    abstract override val `package`: String
    abstract override val name: String
    abstract override val description: String

    @SerialName("stripped_description")
    abstract override val strippedDescription: String
    abstract override val annotations: List<@Contextual DocumentedReference>
    abstract override val deprecated: Boolean

    @SerialName("deprecation_message")
    abstract override val deprecationMessage: String
    abstract override val modifiers: List<String>
    abstract override val metadata: DocumentedObject.Metadata

    sealed class AbstractDocumentedClassObject : AbstractDocumentedObject(),
        DocumentedClassObject {
        abstract override val metadata: ClassMetadataImpl

        @SerialName("CLASS")
        @Serializable
        data class DocumentedClassImpl(
            override val link: String,
            override val description: String,
            @SerialName("stripped_description")
            override val strippedDescription: String,
            override val annotations: List<@Contextual DocumentedReference>,
            override val modifiers: List<String>,
            override val metadata: ClassMetadataImpl,
            override val `package`: String,
            override val name: String,
            override val deprecated: Boolean,
            @SerialName("deprecation_message")
            override val deprecationMessage: String,
        ) : AbstractDocumentedClassObject()

        @SerialName("ENUM")
        @Serializable
        data class DocumentedEnumImpl(
            override val link: String,
            override val description: String,
            @SerialName("stripped_description")
            override val strippedDescription: String,
            override val annotations: List<@Contextual DocumentedReference>,
            override val modifiers: List<String>,
            override val metadata: ClassMetadataImpl,
            override val `package`: String,
            override val name: String,
            override val deprecated: Boolean,
            @SerialName("deprecation_message") override val deprecationMessage: String,
        ) : AbstractDocumentedClassObject()

        @SerialName("INTERFACE")
        @Serializable
        data class DocumentedInterfaceImpl(
            override val link: String,
            override val description: String,
            @SerialName("stripped_description")
            override val strippedDescription: String,
            override val annotations: List<@Contextual DocumentedReference>,
            override val modifiers: List<String>,
            override val metadata: ClassMetadataImpl,
            override val `package`: String,
            override val name: String,
            override val deprecated: Boolean,
            @SerialName("deprecation_message")
            override val deprecationMessage: String,
        ) : AbstractDocumentedClassObject()

        @SerialName("ANNOTATION")
        @Serializable
        data class DocumentedAnnotationImpl(
            override val link: String,
            override val description: String,
            @SerialName("stripped_description")
            override val strippedDescription: String,
            override val annotations: List<@Contextual DocumentedReference>,
            override val modifiers: List<String>,
            override val metadata: ClassMetadataImpl,
            override val `package`: String,
            override val name: String,
            override val deprecated: Boolean,
            @SerialName("deprecation_message")
            override val deprecationMessage: String,
        ) : AbstractDocumentedClassObject()
    }

    internal sealed class AbstractDocumentedMethodObject : AbstractDocumentedObject(),
        DocumentedMethodObject {

        abstract override val metadata: MethodMetadataImpl

        @SerialName("METHOD")
        @Serializable
        data class DocumentedMethodImpl(
            override val link: String,
            override val description: String,
            @SerialName("stripped_description")
            override val strippedDescription: String,
            override val annotations: List<@Contextual DocumentedReference>,
            override val modifiers: List<String>,
            override val metadata: MethodMetadataImpl,
            override val `package`: String,
            override val name: String,
            override val deprecated: Boolean,
            @SerialName("deprecation_message")
            override val deprecationMessage: String,
        ) : AbstractDocumentedMethodObject()

        @SerialName("CONSTRUCTOR")
        @Serializable
        data class DocumentedConstructorImpl(
            override val link: String,
            override val description: String,
            @SerialName("stripped_description")
            override val strippedDescription: String,
            override val annotations: List<@Contextual DocumentedReference>,
            override val modifiers: List<String>,
            override val metadata: MethodMetadataImpl,
            override val `package`: String,
            override val name: String,
            override val deprecated: Boolean,
            @SerialName("deprecation_message")
            override val deprecationMessage: String,
        ) : AbstractDocumentedMethodObject()
    }
}
