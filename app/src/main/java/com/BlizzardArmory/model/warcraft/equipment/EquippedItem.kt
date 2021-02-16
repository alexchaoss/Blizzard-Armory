package com.BlizzardArmory.model.warcraft.equipment

import com.BlizzardArmory.model.common.Media
import com.google.gson.annotations.SerializedName


/**
 * The type Equipped item.
 */
data class EquippedItem(

        @SerializedName("item")
        var item: Item,

        @SerializedName("slot")
        var slot: Slot,

        @SerializedName("quantity")
        var quantity: Int,

        @SerializedName("context")
        var context: Int,

        @SerializedName("bonus_list")
        var bonusList: List<Int>,

        @SerializedName("quality")
        var quality: Quality,

        @SerializedName("name")
        var name: String,

        @SerializedName("modified_appearance_id")
        var modifiedAppearanceId: Long,

        @SerializedName("azerite_details")
        var azeriteDetails: AzeriteDetails,

        @SerializedName("media")
        var media: Media,

        @SerializedName("item_class")
        var itemClass: ItemClass,

        @SerializedName("item_subclass")
        var itemSubclass: ItemSubclass,

        @SerializedName("inventory_type")
        var inventoryType: InventoryType,

        @SerializedName("binding")
        var binding: Binding,

        @SerializedName("armor")
        var armor: Armor,

        @SerializedName("stats")
        var stats: List<Stat>,

        @SerializedName("requirements")
        var requirements: Requirements,

        @SerializedName("level")
        var level: Level,

        @SerializedName("transmog")
        var transmog: Transmog,

        @SerializedName("durability")
        var durability: Durability,

        @SerializedName("unique_equipped")
        var uniqueEquipped: String,

        @SerializedName("spells")
        var spells: List<SpellDescription>,

        @SerializedName("description")
        var description: String,

        @SerializedName("is_subclass_hidden")
        var isIsSubclassHidden: Boolean,

        @SerializedName("set")
        var set: Set,

        @SerializedName("name_description")
        var nameDescriptionObject: NameDescription,

        @SerializedName("socket_bonus")
        var socketBonus: String,

        @SerializedName("sell_price")
        var sellPrice: SellPrice,

        @SerializedName("sockets")
        var sockets: List<Socket>,

        @SerializedName("enchantments")
        var enchantments: List<Enchantment>,

        @SerializedName("weapon")
        var weapon: Weapon

)