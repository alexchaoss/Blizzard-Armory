package com.BlizzardArmory.model.warcraft.realm

import com.BlizzardArmory.model.common.Links
import com.BlizzardArmory.model.common.Realm
import com.google.gson.annotations.SerializedName


data class Realms(

    @SerializedName("_links") val links: Links,
    @SerializedName("realms") val realms: List<Realm>
)