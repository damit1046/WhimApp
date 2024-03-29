package com.android.app.core.functional

import com.android.app.UnitTest
import com.android.app.core.exception.Failure.ServerError
import com.android.app.core.functional.Either.Left
import com.android.app.core.functional.Either.Right
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.junit.Test

class EitherTest : UnitTest() {

    @Test fun `Either Right should return correct type`() {
        val result = Right("ironman")

        result shouldBeInstanceOf Either::class.java
        result.isRight shouldBe true
        result.isLeft shouldBe false
        result.fold({},
                { right ->
                    right shouldBeInstanceOf String::class.java
                    right shouldEqualTo "ironman"
                })
    }

    @Test fun `Either Left should return correct type`() {
        val result = Left("ironman")

        result shouldBeInstanceOf Either::class.java
        result.isLeft shouldBe true
        result.isRight shouldBe false
        result.fold(
                { left ->
                    left shouldBeInstanceOf String::class.java
                    left shouldEqualTo "ironman"
                }, {})
    }

    @Test fun `Either fold should ignore passed argument if it is Right type`() {
        val success = "Success"
        val result = Right(success).getOrElse("Other")

        result shouldEqualTo success
    }

    @Test fun `Either fold should return argument if it is Left type`() {
        val other = "Other"
        val result = Left("Failure").getOrElse(other)

        result shouldEqual other
    }

    @Test
    fun `given fold is called, when either is Right, applies fnR and returns its result`() {
        val either = Right("Success")
        val result = either.fold({ fail("Shouldn't be executed") }) { 5 }

        result shouldBe 5
        either.isRight shouldBe true
    }

    @Test
    fun `given fold is called, when either is Left, applies fnL and returns its result`() {
        val either = Left(12)

        val foldResult = "Fold Result"
        val result = either.fold({ foldResult }) { fail("Shouldn't be executed") }

        result shouldBe foldResult
        either.isLeft shouldBe true
    }

    @Test
    fun `given flatMap is called, when either is Right, applies function and returns new Either`() {
        val either = Right("Success")

        val result = either.flatMap {
            it shouldBe "Success"
            Left(ServerError)
        }

        result shouldEqual Left(ServerError)
        result.isLeft shouldBe true
    }

    @Test
    fun `given flatMap is called, when either is Left, doesn't invoke function and returns original Either`() {
        val either = Left(12)

        val result = either.flatMap { Right(20) }

        result.isLeft shouldBe true
        result shouldEqual either
    }

    @Test
    fun `given onFailure is called, when either is Right, doesn't invoke function and returns original Either`() {
        val success = "Success"
        val either = Right(success)

        val result = either.onFailure { fail("Shouldn't be executed") }

        result shouldBe either
        either.getOrElse("Failure") shouldBe success
    }

    @Test
    fun `given onFailure is called, when either is Left, invokes function with left value and returns original Either`() {
        val either = Left(12)

        var methodCalled = false
        val result = either.onFailure {
            it shouldBe 12
            methodCalled = true
        }

        result shouldBe either
        methodCalled shouldBe true
    }

    @Test
    fun `given onSuccess is called, when either is Right, invokes function with right value and returns original Either`() {
        val success = "Success"
        val either = Right(success)

        var methodCalled = false
        val result = either.onSuccess {
            it shouldEqual success
            methodCalled = true
        }

        result shouldBe either
        methodCalled shouldBe true
    }

    @Test
    fun `given onSuccess is called, when either is Left, doesn't invoke function and returns original Either`() {
        val either = Left(12)

        val result = either.onSuccess {fail("Shouldn't be executed") }

        result shouldBe either
    }

    @Test
    fun `given map is called, when either is Right, invokes function with right value and returns a new Either`() {
        val success = "Success"
        val resultValue = "Result"
        val either = Right(success)

        val result = either.map {
            it shouldBe success
            resultValue
        }

        result shouldEqual Right(resultValue)
    }

    @Test
    fun `given map is called, when either is Left, doesn't invoke function and returns original Either`() {
        val either = Left(12)

        val result = either.map { Right(20) }

        result.isLeft shouldBe true
        result shouldEqual either
    }
}
