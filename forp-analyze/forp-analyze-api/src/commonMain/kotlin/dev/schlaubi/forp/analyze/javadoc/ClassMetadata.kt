package dev.schlaubi.forp.analyze.javadoc

public interface ClassMetadata {
    public val extensions: List<DocumentedReference>
    public val implementations: List<DocumentedReference>
    public val allImplementations: List<DocumentedReference>
    public val superInterfaces: List<DocumentedReference>
    public val subInterfaces: List<DocumentedReference>
    public val subClasses: List<DocumentedReference>
    public val implementingClasses: List<DocumentedReference>
    public val fields: List<DocumentedReference>
    public val methods: List<DocumentedReference>
}
