package com.BlizzardArmory.ui.warcraft.progress

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.warcraft.encounters.EncountersInformation
import com.BlizzardArmory.model.warcraft.encounters.Expansions
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.ui.BaseViewModel
import com.BlizzardArmory.ui.main.MainActivity
import com.BlizzardArmory.util.events.LocaleSelectedEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class ProgressViewModel : BaseViewModel() {

    lateinit var character: String
    lateinit var realm: String
    lateinit var region: String

    private var encounters: MutableLiveData<EncountersInformation> = MutableLiveData()

    fun getEncounters(): LiveData<EncountersInformation> {
        return encounters
    }

    fun downloadEncounterInformation() {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getEncounters(
                character.toLowerCase(Locale.ROOT),
                realm.toLowerCase(Locale.ROOT),
                MainActivity.locale,
                region.toLowerCase(Locale.ROOT),
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    encounters.value = response.body()
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                }
            }
            if (!EventBus.getDefault().isRegistered(this@ProgressViewModel)) {
                EventBus.getDefault().register(this@ProgressViewModel)
            }
        }
        jobs.add(job)
    }


    fun getRaidLevel(expansion: Expansions): String {
        return when (expansion.expansion.id) {
            68L -> "Level 25"
            70L -> "Level 27"
            72L -> "Level 30"
            73L -> "Level 32"
            74L -> "Level 35"
            124L -> "Level 40"
            395L -> "Level 45"
            396L -> "Level 50"
            397L -> "Level 60"
            else -> ""
        }
    }

    @Subscribe
    override fun localeSelectedReceived(LocaleSelectedEvent: LocaleSelectedEvent) {
        super.localeSelectedReceived(LocaleSelectedEvent)
        downloadEncounterInformation()
    }
}