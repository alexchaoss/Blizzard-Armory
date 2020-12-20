package com.BlizzardArmory.network

import com.BlizzardArmory.model.UserInformation
import com.BlizzardArmory.model.diablo.account.AccountInformation
import com.BlizzardArmory.model.diablo.character.CharacterInformation
import com.BlizzardArmory.model.diablo.data.common.Leaderboard
import com.BlizzardArmory.model.diablo.data.eras.index.EraIndex
import com.BlizzardArmory.model.diablo.data.seasons.index.SeasonIndex
import com.BlizzardArmory.model.diablo.item.SingleItem
import com.BlizzardArmory.model.diablo.items.Items
import com.BlizzardArmory.model.starcraft.CurrentSeason
import com.BlizzardArmory.model.starcraft.Player
import com.BlizzardArmory.model.starcraft.leaderboard.LadderTeam
import com.BlizzardArmory.model.starcraft.league.League
import com.BlizzardArmory.model.starcraft.profile.Profile
import com.BlizzardArmory.model.warcraft.account.Account
import com.BlizzardArmory.model.warcraft.achievements.categories.Categories
import com.BlizzardArmory.model.warcraft.achievements.characterachievements.Achievements
import com.BlizzardArmory.model.warcraft.achievements.custom.DetailedAchievements
import com.BlizzardArmory.model.warcraft.charactersummary.CharacterSummary
import com.BlizzardArmory.model.warcraft.covenant.character.soulbind.SoulbindInformation
import com.BlizzardArmory.model.warcraft.covenant.covenant.custom.CovenantSpells
import com.BlizzardArmory.model.warcraft.encounters.EncountersInformation
import com.BlizzardArmory.model.warcraft.equipment.Equipment
import com.BlizzardArmory.model.warcraft.media.Media
import com.BlizzardArmory.model.warcraft.pvp.bracket.BracketStatistics
import com.BlizzardArmory.model.warcraft.pvp.summary.PvPSummary
import com.BlizzardArmory.model.warcraft.pvp.tiers.Tier
import com.BlizzardArmory.model.warcraft.realm.Realms
import com.BlizzardArmory.model.warcraft.reputations.characterreputations.Reputation
import com.BlizzardArmory.model.warcraft.reputations.custom.ReputationPlusParentInfo
import com.BlizzardArmory.model.warcraft.statistic.Statistic
import com.BlizzardArmory.model.warcraft.talents.Talents
import com.BlizzardArmory.model.warcraft.talents.TalentsIcons
import com.BlizzardArmory.network.oauth.TokenResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface NetworkServices {

    @GET("oauth/token")
    suspend fun getAccessToken(@Query("code") clientId: String?,
                               @Query("region") region: String?,
                               @Query("redirect_uri") redirectUri: String?): Response<TokenResponse>

    //User information
    @GET("oauth/userinfo")
    suspend fun getUserInfo(@Query("token") accessToken: String?,
                            @Query("region") region: String?): Response<UserInformation>

    @GET("/data/wow/realm/index")
    suspend fun getRealmIndex(@Query("region") region: String,
                              @Query("namespace") namespace: String,
                              @Query("locale") locale: String,
                              @Query("token") accessToken: String?): Response<Realms>

    //WoW Endpoints
    @GET("/profile/wow/character/{realm}/{character}/character-media")
    suspend fun getMedia(@Path("character", encoded = true) character: String?,
                         @Path("realm", encoded = true) realm: String?,
                         @Query("locale") locale: String?,
                         @Query("region") region: String?,
                         @Query("token") accessToken: String?): Response<Media>

    @GET
    suspend fun getDynamicEquipmentMedia(@Url url: String?,
                                         @Query("locale") locale: String?,
                                         @Query("region") region: String?): Response<com.BlizzardArmory.model.warcraft.equipment.media.Media>

    @GET
    suspend fun getAchievementCategories(@Url url: String?): Response<Categories>

    @GET
    suspend fun getAllAchievements(@Url url: String?): Response<DetailedAchievements>

    @GET
    suspend fun getTalentsWithIcon(@Url url: String?): Response<List<TalentsIcons>>

    @GET
    fun getCovenantSpells(@Url url: String): Call<List<CovenantSpells>>

    @GET
    suspend fun getReputationPlusParentInfo(@Url url: String): Response<List<ReputationPlusParentInfo>>

    @GET("/profile/wow/character/{realm}/{character}/achievements")
    suspend fun getCharacterAchievements(@Path("character", encoded = true) character: String?,
                                         @Path("realm", encoded = true) realm: String?,
                                         @Query("locale") locale: String?,
                                         @Query("region") region: String?,
                                         @Query("token") accessToken: String?): Response<Achievements>

    @GET("profile/wow/character/{realm}/{character}/encounters/raids")
    suspend fun getEncounters(@Path("character", encoded = true) character: String?,
                              @Path("realm", encoded = true) realm: String?,
                              @Query("locale") locale: String?,
                              @Query("region") region: String?,
                              @Query("token") accessToken: String?): Response<EncountersInformation>

    @GET("profile/wow/character/{realm}/{character}/equipment")
    suspend fun getEquippedItems(@Path("character", encoded = true) character: String?,
                                 @Path("realm", encoded = true) realm: String?,
                                 @Query("locale") locale: String?,
                                 @Query("region") region: String?,
                                 @Query("token") accessToken: String?): Response<Equipment>

    @GET("profile/wow/character/{realm}/{character}/statistics")
    suspend fun getStats(@Path("character", encoded = true) character: String?,
                         @Path("realm", encoded = true) realm: String?,
                         @Query("locale") locale: String?,
                         @Query("region") region: String?,
                         @Query("token") accessToken: String?): Response<Statistic>

    @GET("profile/wow/character/{realm}/{character}/specializations")
    suspend fun getSpecs(@Path("character", encoded = true) character: String?,
                         @Path("realm", encoded = true) realm: String?,
                         @Query("locale") locale: String?,
                         @Query("region") region: String?,
                         @Query("token") accessToken: String?): Response<Talents>


    @GET("profile/wow/character/{realm}/{character}")
    suspend fun getCharacter(@Path("character", encoded = true) character: String?,
                             @Path("realm", encoded = true) realm: String?,
                             @Query("locale") locale: String?,
                             @Query("region") region: String?,
                             @Query("token") accessToken: String?): Response<CharacterSummary>

    @GET("profile/user/wow")
    suspend fun getAccount(@Query("locale") locale: String?,
                           @Query("region") region: String?,
                           @Query("token") accessToken: String?): Response<Account>

    @GET("profile/wow/character/{realm}/{character}/pvp-summary")
    suspend fun getPvPSummary(@Path("character", encoded = true) character: String?,
                              @Path("realm", encoded = true) realm: String?,
                              @Query("locale") locale: String?,
                              @Query("region") region: String?,
                              @Query("token") accessToken: String?): Response<PvPSummary>

    @GET("profile/wow/character/{realm}/{character}/pvp-bracket/{BRACKET}")
    suspend fun getPvPBrackets(@Path("character", encoded = true) character: String?,
                               @Path("realm", encoded = true) realm: String?,
                               @Path("BRACKET") bracket: String?,
                               @Query("locale") locale: String?,
                               @Query("region") region: String?,
                               @Query("token") accessToken: String?): Response<BracketStatistics>

    @GET
    suspend fun getDynamicTier(@Url url: String?,
                               @Query("region") region: String?,
                               @Query("locale") locale: String?): Response<Tier>

    @GET("profile/wow/character/{realm}/{character}/reputations")
    suspend fun getReputations(@Path("character", encoded = true) character: String?,
                               @Path("realm", encoded = true) realm: String?,
                               @Query("locale") locale: String?,
                               @Query("region") region: String?,
                               @Query("token") accessToken: String?): Response<Reputation>

    @GET("/profile/wow/character/{realm}/{character}/soulbinds")
    suspend fun getSoulbinds(@Path("character", encoded = true) character: String?,
                             @Path("realm", encoded = true) realm: String?,
                             @Query("locale") locale: String?,
                             @Query("region") region: String?,
                             @Query("token") accessToken: String?): Response<SoulbindInformation>

    //D3 Endpoints
    @GET("d3/profile/{battletag}/")
    suspend fun getD3Profile(@Path("battletag") battletag: String?,
                             @Query("locale") locale: String?,
                             @Query("region") region: String?,
                             @Query("token") accessToken: String?): Response<AccountInformation>

    @GET("d3/profile/{battletag}/hero/{id}")
    suspend fun getD3Hero(@Path("battletag") battletag: String?,
                          @Path("id") id: Long,
                          @Query("locale") locale: String?,
                          @Query("region") region: String?,
                          @Query("token") accessToken: String?): Response<CharacterInformation>

    @GET("d3/profile/{battletag}/hero/{id}/items")
    suspend fun getHeroItems(@Path("battletag") battletag: String?,
                             @Path("id") id: Long,
                             @Query("locale") locale: String?,
                             @Query("region") region: String?,
                             @Query("token") accessToken: String?): Response<Items>

    @GET("d3/data/item/{slug}")
    suspend fun getItem(@Path("slug", encoded = true) slug: String?,
                        @Query("region") region: String?,
                        @Query("locale") locale: String?): Response<SingleItem>

    @GET("data/d3/season/")
    suspend fun getSeasonIndex(@Query("locale") locale: String,
                               @Query("region") region: String): Response<SeasonIndex>

    @GET("data/d3/season/{id}/leaderboard/{leaderboard}")
    suspend fun getSeasonLeaderboard(@Path("id") id: Int,
                                     @Path("leaderboard") leaderboard: String,
                                     @Query("locale") locale: String,
                                     @Query("region") region: String): Response<Leaderboard>

    @GET("data/d3/era/")
    suspend fun getEraIndex(@Query("locale") locale: String,
                            @Query("region") region: String): Response<EraIndex>

    @GET("data/d3/era/{id}/leaderboard/{leaderboard}")
    suspend fun getEraLeaderboard(@Path("id") id: Int,
                                  @Path("leaderboard") leaderboard: String,
                                  @Query("locale") locale: String,
                                  @Query("region") region: String): Response<Leaderboard>

    //Sc2 endpoints
    @GET("sc2/player/{id}")
    suspend fun getSc2Player(@Path("id") id: String?,
                             @Query("locale") locale: String?,
                             @Query("region") region: String?,
                             @Query("token") accessToken: String?): Response<List<Player>>

    @GET("sc2/profile/{region_id}/{realm_id}/{profile_id}")
    suspend fun getSc2Profile(@Path("region_id") regionId: String,
                              @Path("realm_id") realmId: Int,
                              @Path("profile_id") profileId: String?,
                              @Query("locale") locale: String?,
                              @Query("region") region: String?,
                              @Query("token") accessToken: String?): Response<Profile>

    @GET(" sc2/legacy/ladder/{regionId}/{ladderId}")
    suspend fun getSc2LadderLeaderboard(@Path("regionId") regionId: String,
                                        @Path("ladderId") ladderId: Int,
                                        @Query("region") region: String): Response<LadderTeam>

    @GET("data/sc2/league/{seasonId}/{queueId}/{teamType}/{leagueId}")
    suspend fun getSc2League(@Path("seasonId") seasonId: Int,
                             @Path("queueId") queueId: Int,
                             @Path("teamType") teamType: Int,
                             @Path("leagueId") leagueId: Int,
                             @Query("region") region: String): Response<League>

    @GET(" sc2/ladder/season/{regionId}")
    suspend fun getSc2CurrentSeason(@Path("regionId") regoinId: String,
                                    @Query("region") region: String): Response<CurrentSeason>

    //Overwatch endpoint
    @GET
    suspend fun getOWProfile(@Url url: String?): Response<com.BlizzardArmory.model.overwatch.Profile>
}