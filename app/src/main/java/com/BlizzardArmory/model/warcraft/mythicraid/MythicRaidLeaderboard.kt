package com.BlizzardArmory.model.warcraft.mythicraid

import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


data class MythicRaidLeaderboard(

    @SerializedName("_links") val links: Links,
    @SerializedName("slug") val slug: String,
    @SerializedName("criteria_type") val criteria_type: String,
    @SerializedName("entries") val entries: List<Entries>,
    @SerializedName("journal_instance") val journal_instance: JournalInstance
)