package com.BlizzardArmory.model.warcraft.equipment

import com.BlizzardArmory.model.common.Media
import com.google.gson.annotations.SerializedName


/**
 * The type Socket.
 */
class Socket {

    @SerializedName("socket_type")
        var socketType: SocketType? = null

    @SerializedName("item")
        var item: Item? = null

    @SerializedName("context")
        var context = 0

    @SerializedName("display_string")
        var displayString: String? = null

    @SerializedName("media")
        var media: Media? = null

    @SerializedName("bonus_list")
        var bonusList: List<Int>? = null

}