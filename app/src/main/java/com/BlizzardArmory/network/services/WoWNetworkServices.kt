package com.BlizzardArmory.network.services

import com.BlizzardArmory.model.warcraft.account.Account
import com.BlizzardArmory.model.warcraft.achievements.categories.Categories
import com.BlizzardArmory.model.warcraft.achievements.characterachievements.Achievements
import com.BlizzardArmory.model.warcraft.achievements.custom.DetailedAchievements
import com.BlizzardArmory.model.warcraft.charactersummary.CharacterSummary
import com.BlizzardArmory.model.warcraft.covenant.character.soulbind.SoulbindInformation
import com.BlizzardArmory.model.warcraft.covenant.covenant.custom.CovenantSpells
import com.BlizzardArmory.model.warcraft.covenant.techtalent.TechTalentWithIcon
import com.BlizzardArmory.model.warcraft.covenant.techtalenttree.TechTalentTree
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
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface WoWNetworkServices {

    @GET("/data/wow/realm/index")
    suspend fun getRealmIndex(
        @Query("region") region: String,
        @Query("namespace") namespace: String,
        @Query("locale") locale: String,
        @Query("token") accessToken: String?
    ): Response<Realms>

    @GET("/profile/wow/character/{realm}/{character}/character-media")
    suspend fun getMedia(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<Media>

    @GET
    suspend fun getDynamicEquipmentMedia(
        @Url url: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?
    ): Response<com.BlizzardArmory.model.warcraft.equipment.media.Media>

    @GET
    suspend fun getAchievementCategories(@Url url: String?): Response<Categories>

    @GET
    suspend fun getAllAchievements(@Url url: String?): Response<DetailedAchievements>

    @GET
    suspend fun getTalentsWithIcon(@Url url: String?): Response<List<TalentsIcons>>

    @GET
    suspend fun getTechTalentsWithIcon(@Url url: String?): Response<List<TechTalentWithIcon>>

    @GET
    suspend fun getCovenantSpells(@Url url: String): Response<List<CovenantSpells>>

    @GET
    suspend fun getReputationPlusParentInfo(@Url url: String): Response<List<ReputationPlusParentInfo>>

    @GET("/profile/wow/character/{realm}/{character}/achievements")
    suspend fun getCharacterAchievements(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<Achievements>

    @GET("profile/wow/character/{realm}/{character}/encounters/raids")
    suspend fun getEncounters(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<EncountersInformation>

    @GET("profile/wow/character/{realm}/{character}/equipment")
    suspend fun getEquippedItems(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<Equipment>

    @GET("profile/wow/character/{realm}/{character}/statistics")
    suspend fun getStats(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<Statistic>

    @GET("profile/wow/character/{realm}/{character}/specializations")
    suspend fun getSpecs(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<Talents>


    @GET("profile/wow/character/{realm}/{character}")
    suspend fun getCharacter(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<CharacterSummary>

    @GET("profile/user/wow")
    suspend fun getAccount(
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<Account>

    @GET("profile/wow/character/{realm}/{character}/pvp-summary")
    suspend fun getPvPSummary(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<PvPSummary>

    @GET("profile/wow/character/{realm}/{character}/pvp-bracket/{BRACKET}")
    suspend fun getPvPBrackets(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Path("BRACKET") bracket: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<BracketStatistics>

    @GET
    suspend fun getDynamicTier(
        @Url url: String?,
        @Query("region") region: String?,
        @Query("locale") locale: String?
    ): Response<Tier>

    @GET("profile/wow/character/{realm}/{character}/reputations")
    suspend fun getReputations(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<Reputation>

    @GET("/profile/wow/character/{realm}/{character}/soulbinds")
    suspend fun getSoulbinds(
        @Path("character", encoded = true) character: String?,
        @Path("realm", encoded = true) realm: String?,
        @Query("locale") locale: String?,
        @Query("region") region: String?,
        @Query("token") accessToken: String?
    ): Response<SoulbindInformation>

    @GET("/data/wow/tech-talent-tree/{id}")
    suspend fun getTechTree(
        @Path("id") id: Int,
        @Query("locale") locale: String?,
        @Query("region") region: String?
    ): Response<TechTalentTree>

}