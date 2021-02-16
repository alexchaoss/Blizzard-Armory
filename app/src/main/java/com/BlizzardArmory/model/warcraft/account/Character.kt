package com.BlizzardArmory.model.warcraft.account

import com.BlizzardArmory.model.common.Faction
import com.BlizzardArmory.model.common.Realm
import com.google.gson.annotations.SerializedName


/**
 * The type Character.
 */
data class Character(

        @SerializedName("character")
        var characterSelf: CharacterSelf,

        @SerializedName("protected_character")
        var protectedCharacter: ProtectedCharacter,

        @SerializedName("name")
        var name: String,

        @SerializedName("id")
        var id: Long,

        @SerializedName("realm")
        var realm: Realm,

        @SerializedName("playable_class")
        var playableClass: PlayableClass,

        @SerializedName("playable_race")
        var playableRace: PlayableRace,

        @SerializedName("gender")
        var gender: Gender,

        @SerializedName("faction")
        var faction: Faction,

        @SerializedName("level")
        var level: String

)