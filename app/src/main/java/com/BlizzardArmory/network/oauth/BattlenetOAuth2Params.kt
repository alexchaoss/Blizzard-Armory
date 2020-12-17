package com.BlizzardArmory.network.oauth

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.BlizzardArmory.network.oauth.BattlenetQueryParamsAccessMethod.Companion.instance
import com.google.api.client.auth.oauth2.Credential.AccessMethod

open class BattlenetOAuth2Params : Parcelable {
    private val TOKEN_URL_CN = "https://www.battlenet.com.cn/oauth/token"
    private val AUTH_URL_CN = "https://www.battlenet.com.cn/oauth/authorize"
    private val TOKEN_URL_NOT_CN = "https://zone.battle.net/oauth/token"
    private val AUTH_URL_NOT_CN = "https://zone.battle.net/oauth/authorize"
    private val ZONE_DEFAULT = "zone"
    var clientId: String
    var scope: String
    var rederictUri: String
    var userId: String
    var zone: String
    var tokenServerUrl: String
    var authorizationServerEncodedUrl: String

    constructor(clientId: String, zone: String, rederictUri: String, userId: String, vararg scopes: String) {
        this.clientId = clientId
        this.zone = zone

        // If we login in China servers, we have different URL
        if (BattlenetConstants.ZONE_CHINA.equals(zone, ignoreCase = true)) {
            tokenServerUrl = TOKEN_URL_CN
            authorizationServerEncodedUrl = AUTH_URL_CN
        } else {
            tokenServerUrl = TOKEN_URL_NOT_CN.replace(ZONE_DEFAULT, zone)
            authorizationServerEncodedUrl = AUTH_URL_NOT_CN.replace(ZONE_DEFAULT, zone)
        }
        this.rederictUri = rederictUri
        this.userId = userId

        // Create SCOPES with required pattern (Scope+Scope+...)
        val sb = StringBuilder()
        if (null != scopes && scopes.isNotEmpty()) {
            for (scope in scopes) {
                sb.append(scope).append("+")
            }
        } else {
            // Access to WoW and SC2 data
            sb.append(BattlenetConstants.SCOPE_WOW).append("+")
            sb.append(BattlenetConstants.SCOPE_SC2).append("+")
        }
        scope = sb.toString().substring(0, sb.toString().length - 1)
    }

    val accessMethod: AccessMethod
        get() = instance

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(clientId)
        dest.writeString(scope)
        dest.writeString(rederictUri)
        dest.writeString(userId)
        dest.writeString(zone)
        dest.writeString(tokenServerUrl)
        dest.writeString(authorizationServerEncodedUrl)
    }

    constructor(parcel: Parcel) {
        clientId = parcel.readString().toString()
        scope = parcel.readString().toString()
        rederictUri = parcel.readString().toString()
        userId = parcel.readString().toString()
        zone = parcel.readString().toString()
        tokenServerUrl = parcel.readString().toString()
        authorizationServerEncodedUrl = parcel.readString().toString()
    }

    companion object {
        @JvmField
        val CREATOR: Creator<BattlenetOAuth2Params> = object : Creator<BattlenetOAuth2Params> {
            override fun createFromParcel(source: Parcel): BattlenetOAuth2Params {
                return BattlenetOAuth2Params(source)
            }

            override fun newArray(size: Int): Array<BattlenetOAuth2Params?> {
                return arrayOfNulls(size)
            }
        }
    }
}