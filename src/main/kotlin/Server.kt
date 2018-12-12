import org.http4k.core.*
import org.http4k.core.Status.Companion.OK
import org.http4k.filter.ServerFilters
import org.http4k.lens.Query
import org.http4k.lens.int
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Http4kServer
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun MyMathsServer(port: Int): Http4kServer = MyMathsApp().asServer(Jetty(port))

fun MyMathsApp() : HttpHandler = ServerFilters.CatchLensFailure.then(
        routes(
                "/ping" bind Method.GET to { Response(OK) },
                "/add" bind Method.GET to { request ->
                    // the core of the "add" business logic is wrapped up here
                    // TODO: define business logic separate from the routing logic
                    // TODO: consider if that is actually a better design
                    //  Point for debate: does that design boost long-term feature additions or only cut time to production release?
                    val valuesFromRequest = Query.int().multi.defaulted("value", emptyList()).extract(request)
                    Response(OK).body(valuesFromRequest.sum().toString())
                }
        )
)