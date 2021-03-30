package dev.schlaubi.forp.find.internal

// We need to do this as js doesn't have the member
public actual fun MatchResult.findRange(): IntRange? = groups[2]?.range
