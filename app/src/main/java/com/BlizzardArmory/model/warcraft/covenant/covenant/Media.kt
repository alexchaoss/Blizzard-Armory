package com.BlizzardArmory.model.warcraft.covenant.covenant

import com.google.gson.annotations.SerializedName


data class Media (

	@SerializedName("key") val key : Key,
	@SerializedName("id") val id : Int
)