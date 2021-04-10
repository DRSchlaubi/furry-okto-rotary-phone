package dev.schlaubi.forp.analyze.server.javadoc

import dev.schlaubi.forp.analyze.server.Application
import dev.schlaubi.forp.parser.stacktrace.QualifiedClass
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@Location("/javadocs")
private class Javadocs {
    @Location("/packages")
    class Packages(val javadocs: Javadocs)

    @Location("/")
    data class ForClass(val javadocs: Javadocs, val query: String)
}

fun Route.javadocs() {
    location<Javadocs> {
        get<Javadocs.Packages> {
            val packages = Application.analyzer.javadocs.storedPackage

            context.respond(packages)
        }

        get<Javadocs.ForClass> { (_, query) ->
            val (pack, name) = query.split("#")
            val obj = Application.analyzer.javadocs.findDoc(
                QueryQualifiedClass(pack, name)
            ) ?: return@get context.respond(HttpStatusCode.NotFound)

            context.respond(obj)
        }
    }
}

private class QueryQualifiedClass(
    override val packagePath: String?,
    override val className: String
) : QualifiedClass {
    override val innerClasses: List<String> = emptyList()
    override val innerClassName: String? = null
}
