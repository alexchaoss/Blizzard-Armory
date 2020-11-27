package com.BlizzardArmory.model.diablo.item

import com.google.gson.annotations.SerializedName


/**
 * The type Secondary.
 */
data class Secondary(

        @SerializedName("text")

        val text: String,

        @SerializedName("textHtml")

        val textHtml: String

)