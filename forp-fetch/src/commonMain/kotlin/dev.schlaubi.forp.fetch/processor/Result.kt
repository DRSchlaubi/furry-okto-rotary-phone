package dev.schlaubi.forp.fetch.processor

import dev.schlaubi.forp.parser.stacktrace.RootStackTrace

/**
 * Representation of a fetch result.
 *
 * @property result the actual result
 * @property raw the raw string containing the result
 */
public data class Result(val result: RootStackTrace?, val raw: String)
