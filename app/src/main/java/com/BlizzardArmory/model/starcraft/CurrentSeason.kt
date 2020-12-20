package com.BlizzardArmory.model.starcraft

import com.google.gson.annotations.SerializedName

data class CurrentSeason(
        @SerializedName("seasonId")
        val seasonId: Int,
        @SerializedName("number")
        val number: Int,
        @SerializedName("year")
        val year: Int,
        @SerializedName("startDate")
        val startDate: Long,
        @SerializedName("endDate")
        val endDate: Long
)