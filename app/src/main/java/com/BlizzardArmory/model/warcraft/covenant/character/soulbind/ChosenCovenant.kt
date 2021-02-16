package com.BlizzardArmory.model.warcraft.covenant.character.soulbind

import com.BlizzardArmory.model.common.Key
import com.google.gson.annotations.SerializedName


data class ChosenCovenant (

	@SerializedName("id") val id : Int,
	@SerializedName("key") val key : Key,
	@SerializedName("name") val name : String
)