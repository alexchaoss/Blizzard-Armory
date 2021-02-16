package com.BlizzardArmory.model.warcraft.statistic

import com.BlizzardArmory.model.common.Character
import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


/**
 * The type Statistic.
 */
data class Statistic(

        @SerializedName("_links")
        var links: Links,

        @SerializedName("health")
        var health: Int,

        @SerializedName("power")
        var power: Int,

        @SerializedName("power_type")
        var powerType: PowerType,

        @SerializedName("speed")
        var speed: Speed,

        @SerializedName("strength")
        var strength: Strength,

        @SerializedName("agility")
        var agility: Agility,

        @SerializedName("intellect")
        var intellect: Intellect,

        @SerializedName("stamina")
        var stamina: Stamina,

        @SerializedName("melee_crit")
        var meleeCrit: MeleeCrit,

        @SerializedName("melee_haste")
        var meleeHaste: MeleeHaste,

        @SerializedName("mastery")
        var mastery: Mastery,

        @SerializedName("bonus_armor")
        var bonusArmor: Int,

        @SerializedName("lifesteal")
        var lifesteal: Lifesteal,

        @SerializedName("versatility")
        var versatility: Float,

        @SerializedName("versatility_damage_done_bonus")
        var versatilityDamageDoneBonus: Float,

        @SerializedName("versatility_healing_done_bonus")
        var versatilityHealingDoneBonus: Float,

        @SerializedName("versatility_damage_taken_bonus")
        var versatilityDamageTakenBonus: Float,

        @SerializedName("avoidance")
        var avoidance: Avoidance,

        @SerializedName("attack_power")
        var attackPower: Int,

        @SerializedName("main_hand_damage_min")
        var mainHandDamageMin: Float,

        @SerializedName("main_hand_damage_max")
        var mainHandDamageMax: Float,

        @SerializedName("main_hand_speed")
        var mainHandSpeed: Float,

        @SerializedName("main_hand_dps")
        var mainHandDps: Float,

        @SerializedName("off_hand_damage_min")
        var offHandDamageMin: Float,

        @SerializedName("off_hand_damage_max")
        var offHandDamageMax: Float,

        @SerializedName("off_hand_speed")
        var offHandSpeed: Float,

        @SerializedName("off_hand_dps")
        var offHandDps: Float,

        @SerializedName("spell_power")
        var spellPower: Int,

        @SerializedName("spell_penetration")
        var spellPenetration: Int,

        @SerializedName("spell_crit")
        var spellCrit: SpellCrit,

        @SerializedName("mana_regen")
        var manaRegen: Float,

        @SerializedName("mana_regen_combat")
        var manaRegenCombat: Float,

        @SerializedName("armor")
        var armor: Armor,

        @SerializedName("dodge")
        var dodge: Dodge,

        @SerializedName("parry")
        var parry: Parry,

        @SerializedName("block")
        var block: Block,

        @SerializedName("ranged_crit")
        var rangedCrit: RangedCrit,

        @SerializedName("ranged_haste")
        var rangedHaste: RangedHaste,

        @SerializedName("spell_haste")
        var spellHaste: SpellHaste,

        @SerializedName("character")
        var character: Character
)