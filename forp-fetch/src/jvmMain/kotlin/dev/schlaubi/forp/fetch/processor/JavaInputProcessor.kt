package dev.schlaubi.forp.fetch.processor

import dev.schlaubi.forp.fetch.input.Input
import kotlinx.coroutines.future.await
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

/**
 * Wrapper class for [InputProcessor] which doesn't rely on coroutines.
 */
public abstract class JavaInputProcessor<I : Input, O> : InputProcessor<I, O> {

    /**
     * Converts [O] to a List of found [String]s.
     * @see CompletableFuture.completedStage
     */
    protected abstract fun processInputAsync(data: O): CompletionStage<List<String>>

    /**
     * Internal method.
     */
    final override suspend fun fetchInput(data: O): List<String> = processInputAsync(data).await()
}

/**
 * Wrapper class for [MultiInputProcessor] which doesn't rely on coroutines.
 */
public abstract class JavaMultiInputProcessor<I : Input, T> : JavaInputProcessor<I, List<T>>(),
    MultiInputProcessor<I, T>
