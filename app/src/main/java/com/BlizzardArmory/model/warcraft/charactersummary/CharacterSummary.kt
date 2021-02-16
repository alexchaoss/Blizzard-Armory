package com.BlizzardArmory.model.warcraft.charactersummary

import com.BlizzardArmory.model.common.Faction
import com.BlizzardArmory.model.common.Links
import com.BlizzardArmory.model.common.Realm
import com.google.gson.annotations.SerializedName


/**
 * The type Character summary.
 */
data class CharacterSummary(

        @SerializedName("_links")
        var links: Links,

        @SerializedName("id")
        var id: Long,

        @SerializedName("name")
        var name: String,

        @SerializedName("gender")
        var gender: Gender,

        @SerializedName("faction")
        var faction: Faction,

        @SerializedName("race")
        var race: Race,

        @SerializedName("character_class")
        var characterClass: CharacterClass,

        @SerializedName("active_spec")
        var activeSpec: ActiveSpec,

        @SerializedName("realm")
        var realm: Realm,

        @SerializedName("guild")
        var guild: Guild,

        @SerializedName("level")
        var level: Int,

        @SerializedName("experience")
        var experience: Int,

        @SerializedName("achievement_points")
        var achievementPoints: Int,

        @SerializedName("achievements")
        var achievements: Achievements,

        @SerializedName("titles")
        var titles: Titles,

        @SerializedName("pvp_summary")
        var pvpSummary: PvpSummary,

        @SerializedName("raid_progression")
        var raidProgression: RaidProgression,

        @SerializedName("media")
        var media: Media,

        @SerializedName("last_login_timestamp")
        var lastLoginTimestamp: Long,

        @SerializedName("average_item_level")
        var averageItemLevel: Int,

        @SerializedName("equipped_item_level")
        var equippedItemLevel: Int,

        @SerializedName("specializations")
        var specializations: Specializations,

        @SerializedName("statistics")
        var statistics: Statistics,

        @SerializedName("mythic_keystone_profile")
        var mythicKeystoneProfile: MythicKeystoneProfile,

        @SerializedName("equipment")
        var equipment: Equipment,

        @SerializedName("appearance")
        var appearance: Appearance,

        @SerializedName("collections")
        var collections: Collections,

        @SerializedName("active_title")
        var activeTitle: ActiveTitle,

        @SerializedName("reputations")
        var reputations: Reputations

)