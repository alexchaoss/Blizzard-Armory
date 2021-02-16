package com.BlizzardArmory.ui.warcraft.reputations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.warcraft.reputations.characterreputations.RepByExpansion
import com.BlizzardArmory.model.warcraft.reputations.characterreputations.Reputation
import com.BlizzardArmory.model.warcraft.reputations.characterreputations.Reputations
import com.BlizzardArmory.model.warcraft.reputations.custom.ReputationPlusParentInfo
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.network.URLConstants
import com.BlizzardArmory.ui.BaseViewModel
import com.BlizzardArmory.ui.main.MainActivity
import com.BlizzardArmory.util.events.LocaleSelectedEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class ReputationsViewModel : BaseViewModel() {

    lateinit var character: String
    lateinit var realm: String
    lateinit var region: String

    val repsByExpac = arrayListOf<ArrayList<Reputations>>()

    private var reputations: MutableLiveData<Reputation> = MutableLiveData()
    private var reputationsWithParentInfo: MutableLiveData<List<ReputationPlusParentInfo>> = MutableLiveData()

    init {
        for (i in 0..8) {
            repsByExpac.add(arrayListOf())
        }
    }

    fun getReputations(): LiveData<Reputation> {
        return reputations
    }

    fun getReputationsWithParentInfo(): LiveData<List<ReputationPlusParentInfo>> {
        return reputationsWithParentInfo
    }

    fun downloadReputationsPlusParentInfo() {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient()
                .getReputationPlusParentInfo(URLConstants.getReputations(MainActivity.locale))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    reputationsWithParentInfo.value = response.body()
                    downloadReputations()
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                }
            }
        }
        jobs.add(job)
    }

    private fun downloadReputations() {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getReputations(
                character.toLowerCase(Locale.ROOT),
                realm.toLowerCase(Locale.ROOT),
                MainActivity.locale,
                region.toLowerCase(Locale.ROOT),
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    reputations.value = response.body()
                    sortRepsByExpansions(reputations.value!!)
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                    errorCode.value = response.code()
                }
            }
            if (!EventBus.getDefault().isRegistered(this@ReputationsViewModel)) {
                EventBus.getDefault().register(this@ReputationsViewModel)
            }
        }
        jobs.add(job)
    }


    private fun sortRepsByExpansions(reputation: Reputation) {
        repsByExpac.forEach {
            it.clear()
        }
        for (reps in reputation.reputations) {
            for (enumRep in RepByExpansion.values()) {
                if (reps.faction.id == enumRep.id) {
                    when (enumRep.xpac) {
                        "Classic" -> repsByExpac[0].add(reps)
                        "Burning Crusade" -> repsByExpac[1].add(reps)
                        "Wrath of the Lich King" -> repsByExpac[2].add(reps)
                        "Cataclysm" -> repsByExpac[3].add(reps)
                        "Mists of Pandaria" -> repsByExpac[4].add(reps)
                        "Warlords of Draenor" -> repsByExpac[5].add(reps)
                        "Legion" -> repsByExpac[6].add(reps)
                        "Battle for Azeroth" -> repsByExpac[7].add(reps)
                        "Shadowlands" -> repsByExpac[8].add(reps)
                    }
                }
            }
        }
    }

    @Subscribe
    override fun localeSelectedReceived(LocaleSelectedEvent: LocaleSelectedEvent) {
        super.localeSelectedReceived(LocaleSelectedEvent)
        downloadReputationsPlusParentInfo()
    }
}