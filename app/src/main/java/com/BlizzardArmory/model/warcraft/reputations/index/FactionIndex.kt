package com.BlizzardArmory.model.warcraft.reputations.index

import com.BlizzardArmory.model.common.Links
import com.BlizzardArmory.model.warcraft.reputations.Faction
import com.google.gson.annotations.SerializedName


data class FactionIndex(

    @SerializedName("_links") val links: Links,
    @SerializedName("factions") val factions: List<Faction>,
    @SerializedName("root_factions") val root_factions: List<Root_factions>
)