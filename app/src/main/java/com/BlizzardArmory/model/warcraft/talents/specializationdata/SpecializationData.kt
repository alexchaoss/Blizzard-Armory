package com.BlizzardArmory.model.warcraft.talents.specializationdata

import com.BlizzardArmory.model.common.Links
import com.BlizzardArmory.model.common.Media
import com.google.gson.annotations.SerializedName


/**
 * The type Specialization data.
 */
data class SpecializationData(

        @SerializedName("_links")
        var links: Links,

        @SerializedName("id")
        var id: Long,

        @SerializedName("playable_class")
        var playableClass: PlayableClass,

        @SerializedName("name")
        var name: String,

        @SerializedName("gender_description")
        var genderDescription: GenderDescription,

        @SerializedName("media")
        var media: Media,

        @SerializedName("role")
        var role: Role,

        @SerializedName("talent_tiers")
        var talentTiers: List<TalentTier>,

        @SerializedName("pvp_talents")
        var pvpTalents: List<PvpTalent>
)