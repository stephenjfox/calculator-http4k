import com.fox.http4k.Matchers.answerShouldBe
import io.kotlintest.TestContext
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.specs.StringSpec
import org.http4k.client.OkHttp
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.OK
import org.http4k.server.Http4kServer
import java.util.*

class EndToEndSpecification : DescribeSpec({

    val makeServer: (port: Int) -> Http4kServer = ::MyMathsServer
    val randomInt: () -> Int = { Random().nextInt(1000) + 8000 }


    describe("Server interactions") {

        val port = randomInt()
        val server = makeServer(port)
        val client = OkHttp()

        it("should respond to pings", test = serverClosed(server) {
            client(Request(GET, "http://localhost:$port/ping")).status shouldBe OK
            client(Request(GET, "http://localhost:$port/add?value=1&value=2")) answerShouldBe 3
        })
    }

})

class AddFunctionalitySpecification : StringSpec({

    val client = MyMathsApp()

    "adds values together" {
        client(Request(GET, "/add?value=1&value=2")) answerShouldBe 3
    }

    "answer is zero when no values are given" {
        client(Request(GET, "/add")) answerShouldBe 0
    }

    "bad request when some values are non-numbers" {
        client(Request(GET, "/add?value=foo&value=2")).status shouldBe BAD_REQUEST
    }

})


fun serverClosed(server: Http4kServer, function: () -> Unit): TestContext.() -> Unit = {
    server.start()
    function()
    server.stop()
}
