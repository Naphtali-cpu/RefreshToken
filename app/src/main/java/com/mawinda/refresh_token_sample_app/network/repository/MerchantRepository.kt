package com.mawinda.refresh_token_sample_app.network.repository

import android.content.Context
import com.mawinda.refresh_token_sample_app.data.SessionManger
import com.mawinda.refresh_token_sample_app.data.response.LoginResponse
import com.mawinda.refresh_token_sample_app.data.response.ProductResponseItem
import com.mawinda.refresh_token_sample_app.data.response.RefreshTokenResponse
import com.mawinda.refresh_token_sample_app.data.response.ResponseStatus
import com.mawinda.refresh_token_sample_app.network.api.ApiInterface
import com.mawinda.refresh_token_sample_app.network.api.Result
import com.mawinda.refresh_token_sample_app.network.api.SafeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber

class MerchantRepository(private val context: Context) : SafeApiCall() {
    fun api(isAuthorised: Boolean = true) = ApiInterface.invoke(context, isAuthorised)


    fun refreshToken(token: String): ResponseStatus {
        return runBlocking {
            val result = apiRequest {
                api(isAuthorised = false).refreshToken(token)
            }
            when (result) {
                is Result.Success<RefreshTokenResponse> -> {
                    with(result.data) {
                        when (this) {
                            null -> ResponseStatus(isSuccessful = false, msg = "error occured")
                            else -> {
                                SessionManger.saveAccessToken(context, this.accessToken, token)
                                ResponseStatus(isSuccessful = true)
                            }
                        }
                    }
                }
                else -> ResponseStatus(
                    isSuccessful = false,
                    msg = (result as Result.Error).exception.localizedMessage
                )
            }
        }
    }

    suspend fun login(mail: String, password: String): ResponseStatus {
        return withContext(Dispatchers.IO) {
            val result = apiRequest {
                api(isAuthorised = false).login(mail, password)
            }
            when (result) {
                is Result.Success<LoginResponse> -> {
                    with(result.data) {
                        when (this) {
                            null -> ResponseStatus(isSuccessful = false, msg = "error occured")
                            else -> {
                                SessionManger.saveAccessToken(
                                    context = context,
                                    accessToken = this.accessToken,
                                    refreshToken = this.refreshToken
                                )
                                ResponseStatus(isSuccessful = true)
                            }
                        }
                    }

                }
                else -> ResponseStatus(
                    isSuccessful = false,
                    msg = (result as Result.Error).exception.localizedMessage
                )
            }
        }

    }

    suspend fun getData(): ResponseStatus {
        return withContext(Dispatchers.IO) {
            val result = apiRequest {
                api().getData()
            }
            when (result) {
                is Result.Success<List<ProductResponseItem>> -> {
                    Timber.i("Data: ${result.data}")
                    ResponseStatus(isSuccessful = true)
                }
                else -> ResponseStatus(
                    isSuccessful = false,
                    (result as Result.Error).exception.localizedMessage
                )
            }
        }
    }


}