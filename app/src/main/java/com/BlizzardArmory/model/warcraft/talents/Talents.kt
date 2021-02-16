package com.BlizzardArmory.model.warcraft.talents

import com.BlizzardArmory.model.common.Character
import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


/**
 * The type Talents.
 */
data class Talents(

        @SerializedName("_links")
        var links: Links,

        @SerializedName("specializations")
        var specializations: List<Specialization>,

        @SerializedName("active_specialization")
        var activeSpecialization: ActiveSpecialization,

        @SerializedName("character")
        var character: Character

)