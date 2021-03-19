const val version = "0951069063"

fun antlrKotlin(module: String, moduleVersion: String = version): String =
    "com.strumenta.antlr-kotlin:antlr-kotlin-$module:$moduleVersion"
