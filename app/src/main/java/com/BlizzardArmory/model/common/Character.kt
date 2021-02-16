package com.BlizzardArmory.model.common

import com.google.gson.annotations.SerializedName


/**
 * The type Character.
 */
data class Character(

        @SerializedName("key")
        var key: Key,

        @SerializedName("name")
        var name: String,

        @SerializedName("id")
        var id: Long,

        @SerializedName("realm")
        var realm: Realm

)