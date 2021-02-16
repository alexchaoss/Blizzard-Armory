package com.BlizzardArmory.model.common

import com.google.gson.annotations.SerializedName


/**
 * The type Realm.
 */
data class Realm(

        @SerializedName("key")
        var key: Key,

        @SerializedName("name")
        var name: String,

        @SerializedName("id")
        var id: Long,

        @SerializedName("slug")
        var slug: String
)