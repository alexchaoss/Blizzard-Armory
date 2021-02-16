package com.BlizzardArmory.model.starcraft.league

import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


data class League(

    @SerializedName("_links") val Links: Links,
    @SerializedName("key") val key: Key,
    @SerializedName("tier") val tier: List<Tier>
)