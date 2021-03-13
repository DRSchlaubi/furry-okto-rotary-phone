package dev.schlaubi.fopr.core

import dev.schlaubi.fopr.core.annotation.FoprInternals
import org.antlr.v4.runtime.ParserRuleContext

/**
 * Representation of any parsed element
 */
public interface ParsedElement {
    /**
     * Gives you direct access to the [ParserRuleContext] which parsed this element.
     * @see FoprInternals
     */
    @FoprInternals
    public val context: ParserRuleContext

    /**
     * The plain text which was parsed to get this element.
     */
    public val text: String
        get() = context.text
}
