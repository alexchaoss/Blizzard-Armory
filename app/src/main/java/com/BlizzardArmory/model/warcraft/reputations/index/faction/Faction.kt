package com.BlizzardArmory.model.warcraft.reputations.index.faction

import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


data class Faction(

    @SerializedName("_links") val links: Links,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("reputation_tiers") val reputation_tiers: ReputationTiers,
    @SerializedName("factions") val factions: List<Faction>,
    @SerializedName("is_header") val is_header: Boolean
)