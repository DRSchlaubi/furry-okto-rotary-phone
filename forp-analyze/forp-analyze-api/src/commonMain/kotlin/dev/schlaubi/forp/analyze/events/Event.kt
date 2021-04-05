package dev.schlaubi.forp.analyze.events

import dev.schlaubi.forp.analyze.on

/**
 * Interface representing all events.
 *
 * @see on
 */
public interface Event {
    public val type: Type

    public enum class Type(public val serialName: String) {
        EXCEPTION_FOUND("exception_found"),
        JAVADOC_FOUND("javadoc_found"),
        SOURCE_FILE_FOUND("source_file_found");

        public companion object {
            public fun fromName(serialName: String): Type =
                values().first { it.serialName == serialName }
        }
    }
}
