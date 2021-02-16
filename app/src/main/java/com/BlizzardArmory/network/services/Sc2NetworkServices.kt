package com.BlizzardArmory.network.services

import com.BlizzardArmory.model.starcraft.CurrentSeason
import com.BlizzardArmory.model.starcraft.Player
import com.BlizzardArmory.model.starcraft.leaderboard.LadderTeam
import com.BlizzardArmory.model.starcraft.league.League
import com.BlizzardArmory.model.starcraft.profile.Profile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Sc2NetworkServices {

    @GET("sc2/player/{id}")
    suspend fun getSc2Player(
        @Path("id") id: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<List<Player>>

    @GET("sc2/profile/{region_id}/{realm_id}/{profile_id}")
    suspend fun getSc2Profile(
        @Path("region_id") regionId: String,
        @Path("realm_id") realmId: Int,
        @Path("profile_id") profileId: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<Profile>

    @GET(" sc2/legacy/ladder/{regionId}/{ladderId}")
    suspend fun getSc2LadderLeaderboard(
        @Path("regionId") regionId: String,
        @Path("ladderId") ladderId: Int,
        @Query("region") region: String
    ): Response<LadderTeam>

    @GET("data/sc2/league/{seasonId}/{queueId}/{teamType}/{leagueId}")
    suspend fun getSc2League(
        @Path("seasonId") seasonId: Int,
        @Path("queueId") queueId: Int,
        @Path("teamType") teamType: Int,
        @Path("leagueId") leagueId: Int,
        @Query("region") region: String
    ): Response<League>

    @GET(" sc2/ladder/season/{regionId}")
    suspend fun getSc2CurrentSeason(
        @Path("regionId") regoinId: String,
        @Query("region") region: String
    ): Response<CurrentSeason>

}