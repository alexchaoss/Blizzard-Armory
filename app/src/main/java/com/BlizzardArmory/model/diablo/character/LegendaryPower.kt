package com.BlizzardArmory.model.diablo.character

import com.google.gson.annotations.SerializedName


/**
 * The type Legendary power.
 */
data class LegendaryPower(

        @SerializedName("id")
        var id: String,

        @SerializedName("name")
        var name: String,

        @SerializedName("icon")
        var icon: String,

        @SerializedName("displayColor")
        var displayColor: String,

        @SerializedName("tooltipParams")
        var tooltipParams: String

)