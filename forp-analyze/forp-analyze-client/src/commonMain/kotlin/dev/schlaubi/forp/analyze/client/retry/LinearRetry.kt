package dev.schlaubi.forp.analyze.client.retry

//import mu.KotlinLogging
import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.coroutines.delay
import mu.KotlinLogging
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

private val LOG = KotlinLogging.logger { }

/**
 * A linear [Retry] strategy.
 * @property firstBackoff the delay for the first try
 * @property maxBackoff the max delay
 * @property maxTries the maximal amount of tries before giving up
 */
@OptIn(ExperimentalTime::class)
internal class LinearRetry constructor(
    private val firstBackoff: Duration = Duration.seconds(5),
    private val maxBackoff: Duration = Duration.seconds(60),
    private val maxTries: Int = 10,
) : Retry {

    init {
        require(firstBackoff.isPositive()) { "firstBackoff needs to be positive but was ${firstBackoff.inWholeMilliseconds} ms" }
        require(maxBackoff.isPositive()) { "maxBackoff needs to be positive but was ${maxBackoff.inWholeMilliseconds} ms" }
        require(
            maxBackoff.minus(firstBackoff).isPositive()
        ) { "maxBackoff ${maxBackoff.inWholeMilliseconds} ms needs to be bigger than firstBackoff ${firstBackoff.inWholeMilliseconds} ms" }
        require(maxTries > 0) { "maxTries needs to be positive but was $maxTries" }
    }

    private val tries = atomic(0)

    override val hasNext: Boolean
        get() = tries.value < maxTries

    override fun reset() {
        tries.update { 0 }
    }

    override suspend fun retry() {
        if (!hasNext) error("max retries exceeded")

        var diff = (maxBackoff - firstBackoff).inWholeMilliseconds / maxTries
        diff *= tries.incrementAndGet()
        LOG.info { "retry attempt ${tries.value}/$maxTries, delaying for $diff ms." }
        delay(diff)
    }
}
