package dev.schlaubi.forp.analyze.javadoc

public interface MethodMetadata : DocumentedObject.Metadata {
    public val owner: String
    public val parameters: List<MethodParameter>
    public val parameterDescriptions: Map<String, String>
    public val returns: String
    public val returnsDescription: String
    public val throws: List<ThrowsInfo>

    public interface ThrowsInfo {
        public val exception: String

        public val description: String
    }

    public interface MethodParameter {
        public val annotations: List<String>
        public val type: String
        public val name: String
    }
}
