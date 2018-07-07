package com.fox.http4k

import io.kotlintest.shouldBe
import org.http4k.core.Response
import org.http4k.core.Status

object Matchers {

    infix fun Response.answerShouldBe(num: Int) {
        this.status shouldBe Status.OK
        this.bodyString().toInt() shouldBe num
    }

}
