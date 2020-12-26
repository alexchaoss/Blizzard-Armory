package com.BlizzardArmory.network

import com.BlizzardArmory.ui.main.MainActivity

/**
 * The type Url constants.
 */
object URLConstants {
    /**
     * The constant loading.
     */
    @JvmField
    var loading = false
    var HEROKU_AUTHENTICATE = "https://blizzardgamesprofiles.herokuapp.com/"

    /**
     * The constant paypalURL.
     */
    const val PAYPAL_URL = "https://paypal.me/astpierredev"

    /**
     * The constant CALLBACK_URL.
     */
    const val CALLBACK_URL = "https://alexchaoss.github.io/BnetAuthorize"
    const val LOGOUT_URL = "https://battle.net/login/logout"

    /**
     * The constant BASE_URL_USER_INFO.
     */
    const val BASE_URL_USER_INFO = "https://zone.battle.net/"

    /**
     * The constant BASE_URL_CN_USER_INFO.
     */
    const val BASE_URL_CN_USER_INFO = "https://www.battlenet.com.cn"

    /**
     * The constant NOT_FOUND_URL_AVATAR.
     */
    const val NOT_FOUND_URL_AVATAR = "?alt=/wow/static/images/2d/avatar/"

    /**
     * The constant D3_ICON_ITEMS.
     */
    const val D3_ICON_ITEMS = "http://media.blizzard.com/d3/icons/items/large/icon.png"

    /**
     * D3 images
     */
    const val D3_ASSETS = "https://alexchaoss.github.io/BnetAuthorize/img/d3/image.png"

    /**
     * The constant D3_ICON_SKILLS.
     */
    const val D3_ICON_SKILLS = "http://media.blizzard.com/d3/icons/skills/64/url.png"

    /**
     * The constant OW_PROFILE.
     */
    const val OW_PROFILE = "https://ow-api.com/v1/stats/:platform/:region/:battletag/complete"

    /**
     * Overwatch images
     */
    const val OW_ASSETS = "https://alexchaoss.github.io/BnetAuthorize/img/ow/image.png"

    /**
     * SC2 images
     */
    const val SC2_ASSETS = "https://alexchaoss.github.io/BnetAuthorize/img/sc2/image.jpg"

    /**
     * WoW images
     */
    const val WOW_ASSETS = "https://alexchaoss.github.io/BnetAuthorize/img/wow/image.jpg"

    /**
     * Gets base url for user information.
     *
     * @return the base url for user information
     */
    @JvmStatic
    val baseURLforUserInformation: String
        get() = if (MainActivity.selectedRegion.equals("cn", ignoreCase = true)) {
            BASE_URL_CN_USER_INFO
        } else {
            BASE_URL_USER_INFO.replace("zone", MainActivity.selectedRegion.toLowerCase())
        }

    /**
     * Gets region.
     *
     * @return the region
     */
    val region: String
        get() = MainActivity.selectedRegion.toLowerCase()

    /**
     * Gets ow profile.
     *
     * @param username the username
     * @param platform the platform
     * @return the ow profile
     */
    @JvmStatic
    fun getOWProfile(username: String, platform: String): String {
        var url: String
        if (platform.equals("PC", ignoreCase = true)) {
            url = OW_PROFILE.replace(":battletag", username.replace("#", "-"))
            url = url.replace(":platform", "pc")
            url = if (MainActivity.selectedRegion.equals("cn", ignoreCase = true) || MainActivity.selectedRegion.equals("tw", ignoreCase = true)) {
                url.replace(":region", "asia")
            } else {
                url.replace(":region", MainActivity.selectedRegion.toLowerCase())
            }
        } else {
            url = OW_PROFILE.replace(":battletag", username)
            url = url.replace(":platform", platform)
            url = url.replace(":region", "global")
        }
        return url
    }

    @JvmStatic
    fun getOWPortraitImage(character: String): String {
        return OW_ASSETS.replace("image", character + "_portrait")
    }

    fun getOWIconImage(character: String): String {
        return OW_ASSETS.replace("image", character + "_icon")
    }

    fun getD3Asset(name: String): String {
        return D3_ASSETS.replace("image", name)
    }

    fun getSC2Asset(name: String): String {
        return SC2_ASSETS.replace("image", name)
    }

    fun getWoWAsset(name: String): String {
        return WOW_ASSETS.replace("image", name)
    }

    fun selectAchievementsURLFromLocale(locale: String): String {
        return "https://blizzard-armory-server.herokuapp.com/achievements/$locale"
    }

    fun selectAchievementCategoriesURLFromLocale(locale: String): String {
        return "https://blizzard-armory-server.herokuapp.com/categories/$locale"
    }

    fun getTalentsIcons(playableClassId: Int, locale: String): String {
        return "https://blizzard-armory-server.herokuapp.com/talents/$playableClassId/$locale"
    }

    fun getCovenantClassSpells(playableClassId: Int, locale: String): String {
        return "https://blizzard-armory-server.herokuapp.com/covenant/class/$playableClassId/$locale"
    }

    fun getCovenantSpells(covenantId: Int, locale: String): String {
        return "https://blizzard-armory-server.herokuapp.com/covenant/$covenantId/$locale"
    }

    fun getTechTalents(soulbindId: Int, locale: String): String {
        return "https://blizzard-armory-server.herokuapp.com/tech_talents/$soulbindId/$locale"
    }

    fun getReputations(locale: String): String {
        return "https://blizzard-armory-server.herokuapp.com/reputations/$locale"
    }

}