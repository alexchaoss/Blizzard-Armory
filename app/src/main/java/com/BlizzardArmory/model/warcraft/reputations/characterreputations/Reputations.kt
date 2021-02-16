package com.BlizzardArmory.model.warcraft.reputations.characterreputations

import com.BlizzardArmory.model.warcraft.reputations.Faction
import com.google.gson.annotations.SerializedName


data class Reputations(

    @SerializedName("faction") val faction: Faction,
    @SerializedName("standing") val standing: Standing
)