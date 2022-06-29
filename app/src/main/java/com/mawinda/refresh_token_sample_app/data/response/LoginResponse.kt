package com.mawinda.refresh_token_sample_app.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("refresh") val refreshToken: String,
    @field:SerializedName("access") val accessToken: String
)


data class RefreshTokenResponse(
    @field:SerializedName("access") val accessToken: String
)