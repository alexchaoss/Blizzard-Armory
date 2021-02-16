package com.BlizzardArmory.model.common

import com.google.gson.annotations.SerializedName


/**
 * The type Faction.
 */
data class Faction(

        @SerializedName("type")
        var type: String,

        @SerializedName("name")
        var name: String

)