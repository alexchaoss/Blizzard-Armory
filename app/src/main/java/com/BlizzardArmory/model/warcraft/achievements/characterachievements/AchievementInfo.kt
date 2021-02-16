package com.BlizzardArmory.model.warcraft.achievements.characterachievements

import com.BlizzardArmory.model.common.Key
import com.google.gson.annotations.SerializedName


data class AchievementInfo(

        @SerializedName("key") val key: Key,
        @SerializedName("name") val name: String,
        @SerializedName("id") val id: Int
)