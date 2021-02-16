package com.BlizzardArmory.model.warcraft.equipment

import com.BlizzardArmory.model.common.Character
import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


/**
 * The type Equipment.
 */
data class Equipment(

        @SerializedName("_links")
        var links: Links,

        @SerializedName("character")
        var character: Character,

        @SerializedName("equipped_items")
        var equippedItems: List<EquippedItem>,

        @SerializedName("equipped_item_sets")
        var equippedItemSets: List<EquippedItemSet>

)