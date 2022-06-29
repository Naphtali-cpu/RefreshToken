package com.mawinda.refresh_token_sample_app.network.api

import retrofit2.Response
import timber.log.Timber

sealed class Result<out R> {
    data class Success<out T>(val data: T?) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

abstract class SafeApiCall {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call.invoke()
            when{
                response.isSuccessful -> Result.Success(response.body())
                else -> Result.Error(Exception(response.errorBody()?.string()))
            }

        }catch (ex: Exception){
            Timber.e(ex)
            Result.Error(ex)
        }
    }
}