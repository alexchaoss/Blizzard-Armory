package com.BlizzardArmory.network.services

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface OWNetworkServices {

    @GET
    suspend fun getOWProfile(@Url url: String?): Response<com.BlizzardArmory.model.overwatch.Profile>
}