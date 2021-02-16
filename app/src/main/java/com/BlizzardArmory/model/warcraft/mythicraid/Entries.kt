package com.BlizzardArmory.model.warcraft.mythicraid

import com.google.gson.annotations.SerializedName


data class Entries(

    @SerializedName("guild") val guild: Guild,
    @SerializedName("faction") val faction: Faction,
    @SerializedName("timestamp") val timestamp: Int,
    @SerializedName("region") val region: String,
    @SerializedName("rank") val rank: Int
)