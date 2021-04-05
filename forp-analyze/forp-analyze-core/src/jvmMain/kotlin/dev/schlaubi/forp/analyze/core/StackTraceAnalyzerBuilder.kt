package dev.schlaubi.forp.analyze.core

import dev.schlaubi.forp.analyze.JavaStackTraceAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import kotlin.coroutines.CoroutineContext

public actual class StackTraceAnalyzerBuilder : AbstractStackTraceAnalyzerBuilder() {

    override var coroutineDispatcher: CoroutineContext = Dispatchers.IO + Job()

    public fun useExecutorService(executorService: ExecutorService) {
        coroutineDispatcher = executorService.asCoroutineDispatcher()
    }

    @JvmName("build")
    public fun buildJava(): JavaStackTraceAnalyzer = JavaStackTraceAnalyzer(build())
}