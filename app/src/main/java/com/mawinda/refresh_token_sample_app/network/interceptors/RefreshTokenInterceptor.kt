package com.mawinda.refresh_token_sample_app.network.interceptors

import android.content.Context
import com.mawinda.refresh_token_sample_app.data.SessionManger
import com.mawinda.refresh_token_sample_app.data.response.ResponseStatus
import com.mawinda.refresh_token_sample_app.network.repository.MerchantRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class RefreshTokenInterceptor(private val context: Context) : Interceptor {

    private val accessToken by lazy {
        // TODO: replace static access token with: SessionManger.accessToken(context)


        //Test Access Token
        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjM0ODUxMzE3LCJqdGkiOiJlOWNlY2MwYjcyZjA0NjVkYTYyYzkwMDNlNzdlYmQ4ZCIsInVzZXJfaWQiOjEyLCJyb2xlIjoiU1VQUExJRVIiLCJ1c2VybmFtZSI6Im5hcGh0YWxpOTE5QGdtYWlsLmNvbSJ9.TB9VoKba1FZ_7QK10BVVVsm9dJcWYw6FfZjWhfWgYAs"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.addAuthorisation(accessToken)
        val response = chain.proceed(request)

        return when {
            response.code == 401 -> {
                val (isSuccessful, msg) = refreshToken()
                when (isSuccessful) {
                    true -> {

                        //get new token
                        val newToken = SessionManger.accessToken(context)
                        val newRequest = chain.addAuthorisation(newToken)
                        //close previous request
                        response.close()

                        //proceed
                        chain.proceed(newRequest)
                    }
                    else -> throw IOException(msg)
                }
            }
            else -> response
        }

    }

    private fun Interceptor.Chain.addAuthorisation(token: String): Request {
        return this.request().newBuilder().addHeader("Authorization", token).build()
    }

    private fun refreshToken(): ResponseStatus {
        val refreshToken = SessionManger.refreshToken(context)
        return MerchantRepository(context).refreshToken(
            refreshToken
        )

    }


}