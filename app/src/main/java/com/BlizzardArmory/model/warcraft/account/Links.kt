package com.BlizzardArmory.model.warcraft.account

import com.BlizzardArmory.model.common.Self
import com.google.gson.annotations.SerializedName


/**
 * The type Links.
 */
data class Links(

        @SerializedName("self")
        var self: Self,

        @SerializedName("user")
        var user: User,

        @SerializedName("profile")
        var profile: Profile

)