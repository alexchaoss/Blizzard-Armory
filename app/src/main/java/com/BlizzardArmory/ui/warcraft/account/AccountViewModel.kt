package com.BlizzardArmory.ui.warcraft.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.warcraft.account.Account
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.network.URLConstants
import com.BlizzardArmory.ui.BaseViewModel
import com.BlizzardArmory.ui.main.MainActivity
import com.BlizzardArmory.util.events.LocaleSelectedEvent
import com.google.common.eventbus.Subscribe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.util.*

class AccountViewModel : BaseViewModel() {

    private var characters: MutableLiveData<Account> = MutableLiveData()

    fun getCharacters(): LiveData<Account> {
        return characters
    }

    fun downloadWoWCharacters() {
        URLConstants.loading = true
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getAccount(
                MainActivity.locale,
                MainActivity.selectedRegion.toLowerCase(Locale.ROOT),
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    characters.value = response.body()
                    URLConstants.loading = false
                } else {
                    errorCode.value = response.code()
                }
                if (!EventBus.getDefault().isRegistered(this@AccountViewModel)) {
                    EventBus.getDefault().register(this@AccountViewModel)
                }
            }
        }
        jobs.add(job)
    }

    @Subscribe
    override fun localeSelectedReceived(LocaleSelectedEvent: LocaleSelectedEvent) {
        super.localeSelectedReceived(LocaleSelectedEvent)
        downloadWoWCharacters()
    }
}