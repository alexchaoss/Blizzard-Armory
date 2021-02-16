package com.BlizzardArmory.model.warcraft.reputations.index.faction

import com.BlizzardArmory.model.common.Key
import com.google.gson.annotations.SerializedName


data class ReputationTiers(

    @SerializedName("key") val key: Key,
    @SerializedName("id") val id: Int
)