package dev.schlaubi.forp.find.internal

// K/JS doesn't store the range of each group, so we add the prefix of each match in it's own group
// And then count the cars before actual in order to get the range
@Suppress("RedundantNullableReturnType") // inherited
public actual fun MatchResult.findRange(): IntRange? {
    val (prefix, actual) = destructured

    val start = range.first + prefix.length
    val end = start + actual.length

    return start..end
}
