package com.BlizzardArmory.model.warcraft.mythicraid

import com.BlizzardArmory.model.common.Key
import com.google.gson.annotations.SerializedName


data class JournalInstance(

    @SerializedName("key") val key: Key,
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int
)