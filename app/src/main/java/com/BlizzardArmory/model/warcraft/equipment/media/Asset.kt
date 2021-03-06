package com.BlizzardArmory.model.warcraft.equipment.media

import com.google.gson.annotations.SerializedName


/**
 * The type Asset.
 */
data class Asset(

        @SerializedName("key")
        var key: String,

        @SerializedName("value")
        var value: String

)