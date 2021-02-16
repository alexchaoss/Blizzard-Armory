package com.BlizzardArmory.model.warcraft.covenant.conduit

import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


data class Conduit(

    @SerializedName("_links") val links: Links,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("item") val item: Item,
    @SerializedName("socket_type") val socket_type: SocketType,
    @SerializedName("ranks") val ranks: List<Ranks>
)