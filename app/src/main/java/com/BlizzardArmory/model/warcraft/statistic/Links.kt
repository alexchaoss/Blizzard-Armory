package com.BlizzardArmory.model.warcraft.statistic

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * The type Links.
 */
data class Links(

        @SerializedName("self")
        @Expose
        var self: Self

)