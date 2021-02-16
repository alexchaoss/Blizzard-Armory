package com.BlizzardArmory.network.services

import com.BlizzardArmory.model.UserInformation
import com.BlizzardArmory.network.oauth.TokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeneralNetworkServices {

    @GET("oauth/token")
    suspend fun getAccessToken(
        @Query("code") clientId: String?,
        @Query("region") region: String?,
        @Query("redirect_uri") redirectUri: String?
    ): Response<TokenResponse>

    //User information
    @GET("oauth/userinfo")
    suspend fun getUserInfo(
        @Query("token") accessToken: String?,
        @Query("region") region: String?
    ): Response<UserInformation>
}