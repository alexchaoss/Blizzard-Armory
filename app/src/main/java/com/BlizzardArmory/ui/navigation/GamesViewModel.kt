package com.BlizzardArmory.ui.navigation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.UserInformation
import com.BlizzardArmory.model.warcraft.media.Media
import com.BlizzardArmory.model.warcraft.realm.Realms
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.ui.BaseViewModel
import com.BlizzardArmory.ui.main.MainActivity
import com.discord.panels.PanelState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GamesViewModel : BaseViewModel() {

    private var wowRealms: MutableLiveData<MutableMap<String, Realms>> = MutableLiveData()
    private var userInformation: MutableLiveData<UserInformation> = MutableLiveData()
    private var wowMediaCharacter: MutableLiveData<Media> = MutableLiveData()

    data class ViewState(
            val startPanelState: PanelState,
            val endPanelState: PanelState
    )

    private val viewStateSubject: BehaviorSubject<ViewState> =
            BehaviorSubject.createDefault(
                    ViewState(
                            startPanelState = PanelState.Closed,
                            endPanelState = PanelState.Closed
                    )
            )

    init {
        wowRealms.value = mutableMapOf()
    }

    fun observeViewState(): Observable<ViewState> = viewStateSubject

    fun onStartPanelStateChange(panelState: PanelState) {
        val viewState = viewStateSubject.value
        viewStateSubject.onNext(viewState.copy(startPanelState = panelState))
    }

    fun onEndPanelStateChange(panelState: PanelState) {
        val viewState = viewStateSubject.value
        viewStateSubject.onNext(viewState.copy(endPanelState = panelState))
    }

    fun getUserInformation(): LiveData<UserInformation> {
        return userInformation
    }

    fun getMedia(): LiveData<Media> {
        return wowMediaCharacter
    }

    fun getWowRealms(): LiveData<MutableMap<String, Realms>> {
        return wowRealms
    }

    fun downloadUserInfo() {
        val job = coroutineScope.launch {
            val response = RetroClient.getGeneralClient().getUserInfo(
                battlenetOAuth2Helper?.accessToken,
                MainActivity.selectedRegion.toLowerCase(Locale.ROOT)
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    userInformation.value = response.body()
                } else {
                    Log.e("Error", response.message())
                    errorCode.value = response.code()
                }
            }
        }
        jobs.add(job)
    }

    fun getRealms() {
        val jobUS = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getRealmIndex(
                "us",
                "dynamic-us",
                MainActivity.locale,
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    wowRealms.value?.set("US", response.body()!!)
                } else {
                    Log.e("Error", response.message())
                }
            }
        }
        jobs.add(jobUS)
        val jobEU = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getRealmIndex(
                "eu",
                "dynamic-eu",
                MainActivity.locale,
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    wowRealms.value?.set("EU", response.body()!!)
                } else {
                    Log.e("Error", response.message())
                }
            }
        }
        jobs.add(jobEU)
        val jobKR = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getRealmIndex(
                "kr",
                "dynamic-kr",
                MainActivity.locale,
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    wowRealms.value?.set("KR", response.body()!!)
                } else {
                    Log.e("Error", response.message())
                }
            }
        }
        jobs.add(jobKR)
        val jobTW = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getRealmIndex(
                "tw",
                "dynamic-tw",
                MainActivity.locale,
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    wowRealms.value?.set("TW", response.body()!!)
                } else {
                    Log.e("Error", response.message())
                }
            }
        }
        jobs.add(jobTW)
    }

    fun downloadMedia(characterClicked: String, characterRealm: String, selectedRegion: String) {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getMedia(
                characterClicked.toLowerCase(Locale.ROOT),
                characterRealm.toLowerCase(Locale.ROOT),
                MainActivity.locale,
                selectedRegion.toLowerCase(Locale.ROOT),
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    wowMediaCharacter.value = response.body()
                } else {
                    Log.e("Error", response.message())
                    wowMediaCharacter.value = null
                }
            }
        }
        jobs.add(job)
    }
}