package com.chepics.mykotlin

import java.io.IOException
import java.lang.NullPointerException
import kotlin.Exception

fun main() {
    Repository.startFetch()
    getResult(Repository.getCurrentState())
    Repository.finishedFetch()
    getResult(Repository.getCurrentState())
    Repository.error()
    getResult(Repository.getCurrentState())
    Repository.anotherCustomFailure()
    getResult(Repository.getCurrentState())
    Repository.customFailure()
    getResult(Repository.getCurrentState())
}

fun getResult(result: Result) {
    when(result) {
        is Error -> {
            println(result.error.toString())
        }

        is Success -> {
            println(result.dataFetched?: "Ensure you start the fetch function first")
        }

        is Loading -> {
            println("loading")
        }

        is NotLoading -> {
            println("Idle...")
        }
        is Failure.AnotherCustomFailure -> {
            println(result.anotherCustomFailure.toString())
        }
        is Failure.CustomFailure -> {
            println(result.customFailure.toString())
        }
    }
}

object Repository {
    private var loadState: Result = NotLoading
    private var dataFetched: String? = null
    fun startFetch() {
        loadState = Loading
        dataFetched = "data"
    }
    fun finishedFetch() {
        loadState = Success(dataFetched)
        dataFetched = null
    }
    fun error() {
        loadState = Error(Exception("Exception"))
    }
    fun getCurrentState(): Result {
        return loadState
    }
    fun anotherCustomFailure() {
        loadState = Failure.AnotherCustomFailure(NullPointerException("something went wrong!"))
    }
    fun customFailure() {
        loadState = Failure.CustomFailure(IOException("Custom failure"))
    }

}

sealed class Result
data class Success(val dataFetched: String?): Result()
data class Error(val error: Exception?): Result()
object NotLoading: Result()
object Loading: Result()

sealed class Failure: Result() {
    data class CustomFailure(val customFailure: IOException): Failure()
    data class AnotherCustomFailure(val anotherCustomFailure: NullPointerException): Failure()
}

//enum class Result {
//    SUCCESS,
//    ERROR,
//    IDLE,
//    LOADING
//}