package com.BlizzardArmory.ui.diablo.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.diablo.account.AccountInformation
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

class D3ViewModel : BaseViewModel() {

    private var profile: MutableLiveData<AccountInformation> = MutableLiveData()
    private lateinit var battleTag: String
    private lateinit var selectedRegion: String

    fun getProfile(): LiveData<AccountInformation> {
        return profile
    }

    fun downloadAccountInformation(battleTag: String, selectedRegion: String) {
        this.battleTag = battleTag
        this.selectedRegion = selectedRegion
        URLConstants.loading = true
        val job = coroutineScope.launch {
            val response = RetroClient.getD3Client().getD3Profile(
                battleTag,
                MainActivity.locale,
                selectedRegion.toLowerCase(Locale.ROOT),
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    profile.value = response.body()
                    sortHeroes()
                    URLConstants.loading = false
                } else {
                    errorCode.value = response.code()
                    URLConstants.loading = false
                }
            }
            if (!EventBus.getDefault().isRegistered(this@D3ViewModel)) {
                EventBus.getDefault().register(this@D3ViewModel)
            }
        }
        jobs.add(job)
    }

    private fun sortHeroes() {
        profile.value?.heroes = profile.value!!.heroes.sortedBy { it.lastUpdated }.reversed()
    }

    @Subscribe
    override fun localeSelectedReceived(LocaleSelectedEvent: LocaleSelectedEvent) {
        super.localeSelectedReceived(LocaleSelectedEvent)
        downloadAccountInformation(battleTag, selectedRegion)
    }

}