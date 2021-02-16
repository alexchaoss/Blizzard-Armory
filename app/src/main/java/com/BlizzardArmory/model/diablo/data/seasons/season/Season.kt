package com.BlizzardArmory.model.diablo.data.seasons.season

import com.BlizzardArmory.model.common.Links
import com.BlizzardArmory.model.diablo.data.common.LeaderboardInfo
import com.google.gson.annotations.SerializedName


data class Season(

    @SerializedName("_links") val links: Links,
    @SerializedName("leaderboard") val leaderboardInfo: List<LeaderboardInfo>,
    @SerializedName("season_id") val season_id: Int,
    @SerializedName("last_update_time") val last_update_time: String,
    @SerializedName("generated_by") val generated_by: String
)