package com.BlizzardArmory.model.warcraft.covenant.character.soulbind

import com.BlizzardArmory.model.common.Character
import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


data class SoulbindInformation(

    @SerializedName("_links") val links: Links,
    @SerializedName("character") val character: Character,
    @SerializedName("chosen_covenant") val chosenCovenant: ChosenCovenant,
    @SerializedName("renown_level") val renownLevel: Int,
    @SerializedName("soulbinds") val soulbinds: List<Soulbinds>
)