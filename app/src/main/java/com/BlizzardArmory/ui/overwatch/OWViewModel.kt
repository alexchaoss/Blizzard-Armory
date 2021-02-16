package com.BlizzardArmory.ui.overwatch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.overwatch.Profile
import com.BlizzardArmory.model.overwatch.heroes.Hero
import com.BlizzardArmory.model.overwatch.topheroes.TopHero
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.network.URLConstants
import com.BlizzardArmory.ui.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringUtils
import java.util.*

class OWViewModel : BaseViewModel() {

    private val TIME_PLAYED = "Time Played"
    private val GAMES_WON = "Games Won"
    private val WEAPON_ACCURACY = "Weapon Accuracy"
    private val ELIMINATIONS_PER_LIFE = "Eliminations per Life"
    private val MULTIKILL_BEST = "Multikill - Best"
    private val OBJECTIVE_KILLS = "Objective Kills"
    private val sortHeroList = arrayOf(TIME_PLAYED, GAMES_WON, WEAPON_ACCURACY, ELIMINATIONS_PER_LIFE, MULTIKILL_BEST, OBJECTIVE_KILLS)

    private val sortCareerHeroes = arrayListOf<String>()
    private var comp: MutableLiveData<Boolean> = MutableLiveData()

    private var topHeroesQuickPlay: MutableLiveData<ArrayList<TopHero>> = MutableLiveData()
    private var topHeroesCompetitive: MutableLiveData<ArrayList<TopHero>> = MutableLiveData()
    private var careerQuickPlay: MutableLiveData<ArrayList<Hero>> = MutableLiveData()
    private var careerCompetitive: MutableLiveData<ArrayList<Hero>> = MutableLiveData()

    private var profile: MutableLiveData<Profile> = MutableLiveData()

    init {
        topHeroesCompetitive.value = arrayListOf()
        topHeroesQuickPlay.value = arrayListOf()
        careerCompetitive.value = arrayListOf()
        careerQuickPlay.value = arrayListOf()
    }

    fun getProfile(): LiveData<Profile> {
        return profile
    }

    fun getCompToggle(): MutableLiveData<Boolean> {
        return comp
    }

    fun getTopHeroesQuickPlay(): LiveData<ArrayList<TopHero>> {
        return topHeroesQuickPlay
    }

    fun getTopHeroesCompetitive(): LiveData<ArrayList<TopHero>> {
        return topHeroesCompetitive
    }

    fun getCareerQuickPlay(): LiveData<ArrayList<Hero>> {
        return careerQuickPlay
    }

    fun getCareerCompetitive(): LiveData<ArrayList<Hero>> {
        return careerCompetitive
    }

    fun getSortList(): Array<String> {
        return sortHeroList
    }

    fun getCareerSortList(): ArrayList<String> {
        return sortCareerHeroes
    }

    fun downloadAccountInformation(username: String, platform: String) {
        URLConstants.loading = true
        val job = coroutineScope.launch {
            val response = RetroClient.getOWClient()
                .getOWProfile(URLConstants.getOWProfile(username, platform))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    profile.value = response.body()
                    URLConstants.loading = false
                    setTopHeroesLists()
                    setCareerLists()
                    setSpinnerCareerList(careerQuickPlay.value!!)
                    comp.value = false
                } else {
                    errorCode.value = response.code()
                }
            }
        }
        jobs.add(job)
    }

    fun setCareerLists() {
        careerQuickPlay.value = profile.value?.quickPlayStats?.careerStats?.getFullHeroList()!!
        careerCompetitive.value = profile.value?.competitiveStats?.careerStats?.getFullHeroList()!!

        Log.i("TEST", careerQuickPlay.value!!.size.toString() + " + " + careerCompetitive.value!!.size.toString())
    }

    fun setSpinnerCareerList(career: ArrayList<Hero>) {
        sortCareerHeroes.clear()
        sortCareerHeroes.addAll(career.map { it.getName() })
    }

    fun setTopHeroesLists() {
        topHeroesQuickPlay.value = profile.value?.quickPlayStats?.topHeroes?.getFullHeroList()!!
        topHeroesCompetitive.value = profile.value?.competitiveStats?.topHeroes?.getFullHeroList()!!

        sortList(topHeroesCompetitive.value, sortHeroList[0])
        sortList(topHeroesQuickPlay.value, sortHeroList[0])
    }


    fun sortList(topHeroes: ArrayList<TopHero>?, howToSort: String) {
        when (howToSort) {
            TIME_PLAYED -> topHeroes?.sortByDescending { getSeconds(it) }
            GAMES_WON -> topHeroes?.sortByDescending { it.gamesWon }
            WEAPON_ACCURACY -> topHeroes?.sortByDescending { it.weaponAccuracy }
            ELIMINATIONS_PER_LIFE -> topHeroes?.sortByDescending { it.eliminationsPerLife }
            MULTIKILL_BEST -> topHeroes?.sortByDescending { it.multiKillBest }
            OBJECTIVE_KILLS -> topHeroes?.sortByDescending { it.objectiveKills }
        }
    }

    private fun getSeconds(hero: TopHero): Int {
        var secondsHero1 = 0
        when {
            StringUtils.countMatches(hero.timePlayed, ":") == 2 -> {
                secondsHero1 += hero.timePlayed!!.substring(0, hero.timePlayed!!.indexOf(":")).toInt() * 3600
                secondsHero1 += hero.timePlayed!!.substring(hero.timePlayed!!.indexOf(":") + 1, hero.timePlayed!!.lastIndexOf(":")).toInt() * 60
                secondsHero1 += hero.timePlayed!!.substring(hero.timePlayed!!.lastIndexOf(":") + 1).toInt()
            }
            StringUtils.countMatches(hero.timePlayed, ":") == 1 -> {
                secondsHero1 += hero.timePlayed!!.substring(0, hero.timePlayed!!.indexOf(":")).toInt() * 60
                secondsHero1 += hero.timePlayed!!.substring(hero.timePlayed!!.lastIndexOf(":") + 1).toInt()
            }
            else -> {
                secondsHero1 = hero.timePlayed!!.toInt()
            }
        }
        return secondsHero1
    }

}