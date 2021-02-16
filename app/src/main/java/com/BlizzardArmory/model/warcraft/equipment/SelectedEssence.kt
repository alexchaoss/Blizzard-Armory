package com.BlizzardArmory.model.warcraft.equipment

import com.BlizzardArmory.model.common.Media
import com.google.gson.annotations.SerializedName


/**
 * The type Selected essence.
 */
data class SelectedEssence(

        @SerializedName("slot")
        var slot: Int,

        @SerializedName("rank")
        var rank: Int,

        @SerializedName("main_spell_tooltip")
        var mainSpellTooltip: MainSpellTooltip,

        @SerializedName("passive_spell_tooltip")
        var passiveSpellTooltip: PassiveSpellTooltip,

        @SerializedName("essence")
        var essence: Essence,

        @SerializedName("media")
        var media: Media

)