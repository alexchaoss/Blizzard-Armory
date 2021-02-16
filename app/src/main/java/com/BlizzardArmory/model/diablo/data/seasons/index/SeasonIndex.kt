package com.BlizzardArmory.model.diablo.data.seasons.index

import com.BlizzardArmory.model.common.Links
import com.google.gson.annotations.SerializedName


data class SeasonIndex(

    @SerializedName("_links") val links: Links,
    @SerializedName("season") val season: List<Season>,
    @SerializedName("current_season") val current_season: Int,
    @SerializedName("service_current_season") val service_current_season: Int,
    @SerializedName("service_season_state") val service_season_state: String,
    @SerializedName("last_update_time") val last_update_time: String,
    @SerializedName("generated_by") val generated_by: String
)