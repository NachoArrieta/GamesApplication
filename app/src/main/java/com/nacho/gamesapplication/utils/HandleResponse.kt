package com.nacho.gamesapplication.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T,
): Resources<T> {
    return withContext(dispatcher) {
        try {
            Resources.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> Resources.Error("No internet connection")
                is HttpException -> {
                    Resources.Error(message = "Server error ocurred")
                }

                else -> Resources.Error(throwable.message ?: "Unexpected error ocurred")
            }
        }
    }
}