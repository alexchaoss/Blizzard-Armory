package com.BlizzardArmory.model.warcraft.pvp.tiers

import com.BlizzardArmory.model.common.Links
import com.BlizzardArmory.model.common.Media
import com.google.gson.annotations.SerializedName


data class Tier(

    @SerializedName("_links") val links: Links,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("min_rating") val min_rating: Int,
    @SerializedName("max_rating") val max_rating: Int,
    @SerializedName("media") val media: Media,
    @SerializedName("bracket") val bracket: Bracket,
    @SerializedName("rating_type") val rating_type: Int
)