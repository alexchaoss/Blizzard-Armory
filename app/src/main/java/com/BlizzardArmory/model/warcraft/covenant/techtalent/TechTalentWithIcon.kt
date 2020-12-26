package com.BlizzardArmory.model.warcraft.covenant.techtalent

import com.google.gson.annotations.SerializedName


data class TechTalentWithIcon(

        @SerializedName("id") val id: Int,
        @SerializedName("icon") val icon: String,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("cast_time") val cast_time: String,
        @SerializedName("tier") val tier: Int,
        @SerializedName("tree_id") val treeId: Int,
        @SerializedName("display_order") val display_order: Int
)