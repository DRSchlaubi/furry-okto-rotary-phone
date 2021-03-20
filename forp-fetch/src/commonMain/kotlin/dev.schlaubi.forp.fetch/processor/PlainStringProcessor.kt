package dev.schlaubi.forp.fetch.processor

import dev.schlaubi.forp.fetch.input.Input
import dev.schlaubi.forp.fetch.input.PlainStringInput
import dev.schlaubi.forp.fetch.input.StringInput

/**
 * Implementation of [InputProcessor] which just processes the input as plain text.
 */
public class PlainStringProcessor: InputProcessor<PlainStringInput, String> {
    override fun supports(input: Input): Boolean = input is PlainStringInput

    override fun processInput(input: PlainStringInput): String = input.input

    override suspend fun fetchInput(data: String): List<String> = listOf(data)
}
