package com.BlizzardArmory.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.BlizzardArmory.network.URLConstants
import com.BlizzardArmory.network.oauth.BattlenetConstants
import com.BlizzardArmory.network.oauth.BattlenetOAuth2Params
import java.util.*

class MainViewModel : ViewModel() {

    private var battlenetOAuth2Params: MutableLiveData<BattlenetOAuth2Params> = MutableLiveData()
    private var clientID: String? = "339a9ad89d9f4acf889b025ccb439567"

    fun getBattlenetOAuth2Params(): LiveData<BattlenetOAuth2Params> {
        return battlenetOAuth2Params
    }

    fun openLoginToBattleNet() {
        battlenetOAuth2Params.value = BattlenetOAuth2Params(clientID!!, MainActivity.selectedRegion.toLowerCase(Locale.ROOT),
                URLConstants.CALLBACK_URL, "Blizzard Games Profiles", BattlenetConstants.SCOPE_WOW, BattlenetConstants.SCOPE_SC2)
    }
}