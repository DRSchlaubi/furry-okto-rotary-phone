package dev.schlaubi.forp.fetch.utils

import kotlinx.coroutines.coroutineScope

internal suspend fun <I, O> Iterable<I>.asyncMap(mapper: suspend (I) -> O): List<O> {
    val output = ArrayList<O>(collectionSizeOrDefault(10))
    coroutineScope {
        forEach {
            output.add(mapper(it))
        }
    }

    return output.toList()
}

internal fun <T> Iterable<T>.collectionSizeOrDefault(default: Int): Int =
    if (this is Collection<*>) this.size else default

