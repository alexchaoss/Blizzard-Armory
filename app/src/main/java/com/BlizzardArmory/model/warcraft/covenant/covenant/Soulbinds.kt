package com.BlizzardArmory.model.warcraft.covenant.covenant

import com.BlizzardArmory.model.common.Key
import com.google.gson.annotations.SerializedName


data class Soulbinds (

	@SerializedName("key") val key : Key,
	@SerializedName("name") val name : String,
	@SerializedName("id") val id : Int
)