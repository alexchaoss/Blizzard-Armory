package com.BlizzardArmory.ui.starcraft.leaderboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.starcraft.CurrentSeason
import com.BlizzardArmory.model.starcraft.leaderboard.LadderMembers
import com.BlizzardArmory.model.starcraft.league.League
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.network.URLConstants
import com.BlizzardArmory.ui.BaseViewModel
import com.BlizzardArmory.util.events.LocaleSelectedEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.Subscribe

class SC2LeaderboardViewModel : BaseViewModel() {

    private var league: MutableLiveData<League> = MutableLiveData()
    private var season: MutableLiveData<CurrentSeason> = MutableLiveData()
    private var leaderboard: MutableLiveData<List<List<LadderMembers>>> = MutableLiveData()

    var teamType: Int = 0
    lateinit var queueId: String
    var seasonId: Int = 0
    lateinit var leagueString: String

    fun getLeague(): LiveData<League> {
        return league
    }

    fun getCurrentSeason(): LiveData<CurrentSeason> {
        return season
    }

    fun getLeaderboard(): LiveData<List<List<LadderMembers>>> {
        return leaderboard
    }

    fun downloadLeaderboard(regionId: Int, ladderId: Int, region: String) {
        val job = coroutineScope.launch {
            val response = RetroClient.getSc2Client()
                .getSc2LadderLeaderboard(parseRegionId(regionId), ladderId, region)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val board = response.body()
                    leaderboard.value = board!!.ladderMembers.groupBy { Triple(it.joinTimestamp, it.wins, it.losses) }.map { it.value }
                    URLConstants.loading = false
                } else {
                    URLConstants.loading = false
                    errorCode.value = response.code()
                }
            }
        }
        jobs.add(job)
    }

    fun downloadLeague(region: String) {
        val job = coroutineScope.launch {
            val response = RetroClient.getSc2Client()
                .getSc2League(seasonId, getQueueAsInt(), teamType, getLeagueAsInt(), region)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    league.value = response.body()
                } else {
                    URLConstants.loading = false
                    errorCode.value = response.code()
                }
            }
        }
        jobs.add(job)
    }

    fun downloadCurrentSeason(regionId: Int, region: String) {
        val job = coroutineScope.launch {
            val response =
                RetroClient.getSc2Client().getSc2CurrentSeason(parseRegionId(regionId), region)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    season.value = response.body()
                } else {
                    URLConstants.loading = false
                    errorCode.value = response.code()
                }
            }
        }
        jobs.add(job)
    }

    @Subscribe
    override fun localeSelectedReceived(LocaleSelectedEvent: LocaleSelectedEvent) {
        super.localeSelectedReceived(LocaleSelectedEvent)

    }

    private fun getQueueAsInt(): Int {
        when (queueId) {
            "1v1" -> return 201
            "2v2" -> return 202
            "3v3" -> return 203
            "4v4" -> return 204
        }
        return 201
    }

    private fun getLeagueAsInt(): Int {
        when (leagueString) {
            "Bronze" -> return 0
            "Silver" -> return 1
            "Gold" -> return 2
            "Platinum" -> return 3
            "Diamond" -> return 4
            "Master" -> return 5
            "Grandmaster" -> return 6
        }
        return 6
    }

    private fun parseRegionId(regionId: Int): String {
        when (regionId) {
            1 -> return "US"
            2 -> return "EU"
            3 -> return "KO"
            5 -> return "CN"
        }
        return "US"
    }
}