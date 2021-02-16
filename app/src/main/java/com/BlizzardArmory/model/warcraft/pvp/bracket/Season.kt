package com.BlizzardArmory.model.warcraft.pvp.bracket

import com.BlizzardArmory.model.common.Key
import com.google.gson.annotations.SerializedName


data class Season(

        @SerializedName("key") val key: Key,
        @SerializedName("id") val id: Int
)