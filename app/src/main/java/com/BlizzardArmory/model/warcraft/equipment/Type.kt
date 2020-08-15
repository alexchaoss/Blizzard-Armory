package com.BlizzardArmory.model.warcraft.equipment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * The type Type.
 */
data class Type(

        @SerializedName("type")
        @Expose
        var type: String,

        @SerializedName("name")
        @Expose
        var name: String

)