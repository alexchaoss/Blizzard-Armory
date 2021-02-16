package com.BlizzardArmory.model.warcraft.encounters

import com.BlizzardArmory.model.common.Character
import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


data class EncountersInformation(

    @SerializedName("_links") val links: Links,
    @SerializedName("character") val character: Character,
    @SerializedName("expansions") val expansions: List<Expansions>
)