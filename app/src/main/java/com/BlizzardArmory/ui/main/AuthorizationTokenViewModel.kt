package com.BlizzardArmory.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.network.oauth.BattlenetConstants
import com.BlizzardArmory.ui.BaseViewModel
import com.google.api.client.auth.oauth2.TokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.util.*

class AuthorizationTokenViewModel : BaseViewModel() {

    private var handled = false
    private var hasLoggedIn: MutableLiveData<Boolean> = MutableLiveData()
    private var startActivity: MutableLiveData<Boolean> = MutableLiveData()

    init {
        hasLoggedIn.value = false
    }

    fun isHandled(): Boolean {
        return handled
    }

    fun hasLoggedIn(): LiveData<Boolean> {
        return hasLoggedIn
    }

    fun startActivity(): LiveData<Boolean> {
        return startActivity
    }

    suspend fun processToken(url: String) {
        if (url.startsWith(getBnetParams().value!!.rederictUri)) {
            Log.i(BattlenetConstants.TAG, "Redirect URL found: $url")
            handled = true
            try {
                if (url.contains("code=")) {
                    val authorizationCode = extractCodeFromUrl(url)
                    Log.i(BattlenetConstants.TAG, "Found code = $authorizationCode")
                    val token = retrieveToken(authorizationCode)
                    battlenetOAuth2Helper!!.storeAccessToken(token)
                    startActivity.value = true
                    Log.i("GOT HERE", "LOGIN")
                    hasLoggedIn.value = true
                } else if (url.contains("error=") || battlenetOAuth2Helper?.accessToken == null) {
                    startActivity.value = false
                }
            } catch (e: Exception) {
                Log.e(BattlenetConstants.TAG, "Error processing token", e)
            }
        } else {
            Log.i(BattlenetConstants.TAG, "Not doing anything for url $url")
        }
    }

    private suspend fun retrieveToken(authorizationCode: String): TokenResponse {
        Log.i(BattlenetConstants.TAG, "retrieveAndStoreAccessToken for code $authorizationCode")
        val token = TokenResponse()
        val job = coroutineScope.launch {
            val response = RetroClient.getGeneralClient().getAccessToken(
                authorizationCode,
                getBnetParams().value!!.zone.toLowerCase(Locale.ROOT),
                getBnetParams().value!!.rederictUri
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val tokenResponse = response.body()
                    token.expiresInSeconds = tokenResponse?.expiresIn
                    token.accessToken = tokenResponse?.accessToken
                    token.scope = tokenResponse?.scope
                    token.tokenType = tokenResponse?.tokenType
                    Log.i(BattlenetConstants.TAG, "Found tokenResponse: " + token.accessToken)
                    if (null != token.accessToken) {
                        Log.i(BattlenetConstants.TAG, "Access Token : " + token.accessToken)
                    }
                    if (null != token.refreshToken) {
                        Log.i(BattlenetConstants.TAG, "Refresh Token : " + token.refreshToken)
                    }
                    Log.i("TOKEN", tokenResponse.toString())
                } else {
                    startActivity.value = false
                }
            }
        }
        jobs.add(job)
        job.join()
        return token
    }

    private fun extractCodeFromUrl(url: String): String {
        val encodedCode = url.substring(getBnetParams().value!!.rederictUri.length + 7, url.length)
        return URLDecoder.decode(encodedCode, "UTF-8")
    }
}