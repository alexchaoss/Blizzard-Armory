package com.BlizzardArmory.model.warcraft.account

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * The type Playable race.
 */
data class PlayableRace(

        @SerializedName("key")
        @Expose
        var key: Key,

        @SerializedName("name")
        @Expose
        var name: String,

        @SerializedName("id")
        @Expose
        var id: Long

)