package com.BlizzardArmory.model.warcraft.achievements.characterachievements

import com.BlizzardArmory.model.common.Character
import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


data class Achievements(

        @SerializedName("_links") val Links: Links,
        @SerializedName("total_quantity") val total_quantity: Int,
        @SerializedName("total_points") val total_points: Int,
        @SerializedName("achievements") val achievements: List<Achievement>,
        @SerializedName("character") val character: Character,
        @SerializedName("statistics") val statistics: Statistics
)