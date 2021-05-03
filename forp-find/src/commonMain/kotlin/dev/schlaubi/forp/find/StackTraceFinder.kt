package dev.schlaubi.forp.find

import dev.schlaubi.forp.core.StackTraceParser
import dev.schlaubi.forp.core.stacktrace.ParsedRootStackTrace
import dev.schlaubi.forp.find.internal.findRange
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

@Suppress("unused", "MemberVisibilityCanBePrivate")
public object StackTraceFinder {
    /**
     * Regex used to find the start of a stack trace.
     */
    // https://regex101.com/r/VK4ssu/3
    @JvmField
    public val STACK_TRACE_START: Regex =
        "(^(?:Exception in thread \".*\")?\\s*?)(.+?(?<=Exception|Error:))"
            .toRegex(RegexOption.MULTILINE)

    /**
     * Searches for [stack traces][ParsedRootStackTrace] in [input].
     */
    @JvmStatic
    public fun findStackTraces(input: CharSequence): List<ParsedRootStackTrace> {
        val possibleStackTraces = STACK_TRACE_START.findAll(input).toList()

        val stackTraces = ArrayList<ParsedRootStackTrace>(possibleStackTraces.size)

        var currentInput = input
        var currentEnd = 0
        var lastStart = 0
        for (start in possibleStackTraces) {
            // We should only start parsing the actual exception,
            // therefore we start at the first group
            val range = start.findRange() ?: continue
            val currentStart = range.first
            if (currentStart < currentEnd) continue

            // Because we always update the base string the index gets shifted
            currentInput = currentInput.substring(currentStart - lastStart)
            val stackTrace = StackTraceParser.parse(currentInput)
            currentEnd = currentStart + 1 + stackTrace.context.stop!!.stopIndex
            lastStart = currentStart
            if (stackTrace.elements.isNotEmpty()) {
                stackTraces.add(stackTrace)
            }
        }

        return stackTraces.toList()
    }
}
