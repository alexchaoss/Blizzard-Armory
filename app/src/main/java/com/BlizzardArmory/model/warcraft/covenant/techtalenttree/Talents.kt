package com.BlizzardArmory.model.warcraft.covenant.techtalenttree

import com.google.gson.annotations.SerializedName


data class Talents (

	@SerializedName("key") val key : Key,
	@SerializedName("name") val name : String,
	@SerializedName("id") val id : Int,
)