package dev.schlaubi.forp.analyze.core.utils

import dev.schlaubi.forp.analyze.SourceFile
import dev.schlaubi.forp.analyze.core.SourceFileImpl
import dev.schlaubi.forp.find.internal.findRange

internal object ClassFinder {
    // https://regex101.com/r/hYZOFr/4
    private val JVM_CLASS_START_PATTERN =
        """((?:package\s*(?:\w+\.?)+;[\s\S]*?)?(?=class)class\s*)(\w+)""".toRegex()

    private const val openingBracket = '{'
    private const val closingBracket = '}'
    private const val import = "import"

    fun findClasses(input: CharSequence): List<SourceFile> {
        val possibleStackTraces = JVM_CLASS_START_PATTERN.findAll(input).toList()

        val stackTraces = ArrayList<SourceFile>(possibleStackTraces.size)

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
            var brackets = -1
            val classContent = currentInput.takeWhile {
                if (it == openingBracket) {
                    brackets += brackets.positivize()
                } else if (it == closingBracket) {
                    brackets--
                }

                brackets != 0
            }

            val (prefix, name) = start.destructured

            val completeContent = prefix + classContent

            val completeStructure = brackets == 0
            val hasImports = completeContent.contains(import)
            val potentiallyIncomplete = !(completeStructure && hasImports)

            currentEnd = currentStart + classContent.length
            lastStart = currentStart

            stackTraces.add(SourceFileImpl(name, completeContent, potentiallyIncomplete))
        }

        return stackTraces.toList()
    }
}

private fun Int.positivize() = if (this < 0) 2 else 1