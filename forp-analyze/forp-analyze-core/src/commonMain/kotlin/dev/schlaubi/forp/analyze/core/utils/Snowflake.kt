package dev.schlaubi.forp.analyze.core.utils

import kotlinx.datetime.Clock
import kotlin.jvm.JvmOverloads
import kotlin.jvm.Synchronized
import kotlin.jvm.Volatile


/**
 * Distributed Sequence Generator.
 * Inspired by Twitter snowflake: https://github.com/twitter/snowflake/tree/snowflake-2010
 *
 * This class should be used as a Singleton.
 * Make sure that you create and reuse a Single instance of Snowflake per node in your distributed system cluster.
 */
public open class Snowflake {
    private val nodeId: Long
    private val customEpoch: Long

    @Volatile
    private var lastTimestamp = -1L

    @Volatile
    private var sequence = 0L

    // Create Snowflake with a nodeId and custom epoch
    // Create Snowflake with a nodeId
    @JvmOverloads
    public constructor(nodeId: Long, customEpoch: Long = DEFAULT_CUSTOM_EPOCH) {
        if (nodeId < 0 || nodeId > maxNodeId) {
            throw IllegalArgumentException(
                "NodeId must be between 0 and $maxNodeId"
            )
        }
        this.nodeId = nodeId
        this.customEpoch = customEpoch
    }

    // Let Snowflake generate a nodeId
    public constructor() {
        nodeId = createNodeId(maxNodeId)
        customEpoch = DEFAULT_CUSTOM_EPOCH
    }

    @Synchronized
    public fun nextId(): Long {
        var currentTimestamp = timestamp()
        check(currentTimestamp >= lastTimestamp) { "Invalid System Clock!" }
        if (currentTimestamp == lastTimestamp) {
            sequence = sequence + 1 and maxSequence
            if (sequence == 0L) {
                // Sequence Exhausted, wait till next millisecond.
                currentTimestamp = waitNextMillis(currentTimestamp)
            }
        } else {
            // reset sequence to start with zero for the next millisecond
            sequence = 0
        }
        lastTimestamp = currentTimestamp
        return (currentTimestamp shl NODE_ID_BITS + SEQUENCE_BITS or (nodeId shl SEQUENCE_BITS)
                or sequence)
    }

    // Get current timestamp in milliseconds, adjust for the custom epoch.
    private fun timestamp(): Long {
        return Clock.System.now().toEpochMilliseconds() - customEpoch
    }

    // Block and wait till next millisecond
    private fun waitNextMillis(currentTimestamp: Long): Long {
        var current = currentTimestamp
        while (current == lastTimestamp) {
            current = timestamp()
        }
        return currentTimestamp
    }

    public fun parse(id: Long): LongArray {
        val maskNodeId = (1L shl NODE_ID_BITS) - 1 shl SEQUENCE_BITS
        val maskSequence = (1L shl SEQUENCE_BITS) - 1
        val timestamp = (id shr NODE_ID_BITS + SEQUENCE_BITS) + customEpoch
        val nodeId = id and maskNodeId shr SEQUENCE_BITS
        val sequence = id and maskSequence
        return longArrayOf(timestamp, nodeId, sequence)
    }

    override fun toString(): String {
        return ("Snowflake Settings [EPOCH_BITS=" + EPOCH_BITS + ", NODE_ID_BITS=" + NODE_ID_BITS
                + ", SEQUENCE_BITS=" + SEQUENCE_BITS + ", CUSTOM_EPOCH=" + customEpoch
                + ", NodeId=" + nodeId + "]")
    }

    public companion object : Snowflake() {
        private const val UNUSED_BITS = 1 // Sign bit, Unused (always set to 0)
        private const val EPOCH_BITS = 41
        private const val NODE_ID_BITS = 10
        private const val SEQUENCE_BITS = 12
        private const val maxNodeId = (1L shl NODE_ID_BITS) - 1
        private const val maxSequence = (1L shl SEQUENCE_BITS) - 1

        // Custom Epoch (January 1, 2015 Midnight UTC = 2015-01-01T00:00:00Z)
        private const val DEFAULT_CUSTOM_EPOCH = 1420070400000L
    }
}

internal expect fun createNodeId(maxNodeId: Long): Long
