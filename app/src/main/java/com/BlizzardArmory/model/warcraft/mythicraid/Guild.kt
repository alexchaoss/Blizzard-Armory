package com.BlizzardArmory.model.warcraft.mythicraid

import com.BlizzardArmory.model.common.Realm
import com.google.gson.annotations.SerializedName


data class Guild(

    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("realm") val realm: Realm
)