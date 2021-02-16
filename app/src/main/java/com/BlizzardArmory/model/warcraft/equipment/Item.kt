package com.BlizzardArmory.model.warcraft.equipment

import com.BlizzardArmory.model.common.Key
import com.google.gson.annotations.SerializedName


/**
 * The type Item.
 */
data class Item(

        @SerializedName("key")
        var key: Key,

        @SerializedName("id")
        var id: Long,

        @SerializedName("name")
        var name: String?

)