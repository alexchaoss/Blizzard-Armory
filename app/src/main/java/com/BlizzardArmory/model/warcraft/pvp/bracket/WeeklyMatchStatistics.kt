package com.BlizzardArmory.model.warcraft.pvp.bracket

import com.google.gson.annotations.SerializedName


data class WeeklyMatchStatistics(

        @SerializedName("played") val played: Int,
        @SerializedName("won") val won: Int,
        @SerializedName("lost") val lost: Int
)