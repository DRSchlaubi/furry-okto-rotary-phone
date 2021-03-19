package dev.schlaubi.forp.core

import dev.schlaubi.forp.core.annotation.ForpInternals
import org.antlr.v4.kotlinruntime.ParserRuleContext

/**
 * Representation of any parsed element
 */
public interface ParsedElement {
    /**
     * Gives you direct access to the [ParserRuleContext] which parsed this element.
     * @see ForpInternals
     */
    @ForpInternals
    public val context: ParserRuleContext

    /**
     * The plain text which was parsed to get this element.
     */
    public val text: String
        get() = context.text
}
