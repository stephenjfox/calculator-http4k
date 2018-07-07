import io.kotlintest.TestContext
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Http4kServer
import java.util.*

class EndToEndSpecification : DescribeSpec({

    val makeServer: (port: Int) -> Http4kServer = ::MyMathServer
    val randomInt: () -> Int = { Random().nextInt(1000) + 8000 }


    describe("Server interactions") {

        val port = randomInt()
        val server = makeServer(port)
        val client = OkHttp()

        it("should respond to pings", test = serverClosed(server) {
            client(Request(GET, "http:localhost:$port/ping")).status shouldBe OK
        })
    }

})

fun serverClosed(server: Http4kServer, function: () -> Unit): TestContext.() -> Unit = {
    server.start()
    function()
    server.stop()
}
