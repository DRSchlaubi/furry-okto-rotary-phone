package dev.schlaubi.forp.analyze.server.javadoc

import dev.schlaubi.forp.analyze.server.Application
import dev.schlaubi.forp.parser.stacktrace.QualifiedClass
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@Location("/javadocs")
class Javadocs {
    @Location("/packages")
    class Packages(val javadocs: Javadocs)

    @Location("/{query}")
    data class ForClass(val query: String, val javadocs: Javadocs)
}

fun Route.javadocs() {
    get<Javadocs.Packages> {
        val packages = Application.analyzer.javadocs.storedPackage

        context.respond(packages)
    }

    get<Javadocs.ForClass> { (query) ->
        val pack = query.substringBeforeLast(".")
        val name = query.substring(pack.length)
        val obj = Application.analyzer.javadocs.findDoc(
            QueryQualifiedClass(pack, name)
        ) ?: return@get context.respond(HttpStatusCode.NotFound)

        context.respond(obj)
    }
}

private class QueryQualifiedClass(
    override val packagePath: String?,
    override val className: String
) : QualifiedClass {
    override val innerClasses: List<String> = emptyList()
    override val innerClassName: String? = null
}
