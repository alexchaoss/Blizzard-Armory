package com.BlizzardArmory.ui.diablo.characterfragments.gear

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.diablo.items.Item
import com.BlizzardArmory.model.diablo.items.Items
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

class CharacterGearViewModel : BaseViewModel() {

    private var itemsInformation: MutableLiveData<Items> = MutableLiveData()
    private var itemsInfoSetup: MutableLiveData<Boolean> = MutableLiveData()

    private val items = arrayListOf<Item>()
    private val primaryStatsMap = mutableMapOf<Int, String>()
    private val secondaryStatsMap = mutableMapOf<Int, String>()
    private val gemsMap = mutableMapOf<Int, String>()

    private var battletag = ""
    private var selectedRegion = ""
    private var id = 0L

    fun getItemsInformation(): LiveData<Items> {
        return itemsInformation
    }

    fun getItemsInfoSetup(): LiveData<Boolean> {
        return itemsInfoSetup
    }

    fun getItems(): ArrayList<Item> {
        return items
    }

    fun getprimaryStats(): Map<Int, String> {
        return primaryStatsMap
    }

    fun getsecondaryStats(): Map<Int, String> {
        return secondaryStatsMap
    }

    fun getgems(): Map<Int, String> {
        return gemsMap
    }

    fun downloadItemInformation(battletag: String, id: Long, selectedRegion: String) {
        this.battletag = battletag
        this.id = id
        this.selectedRegion = selectedRegion
        val job = coroutineScope.launch {
            val response = RetroClient.getD3Client().getHeroItems(
                battletag,
                id,
                MainActivity.locale,
                selectedRegion.toLowerCase(Locale.ROOT),
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    itemsInformation.value = response.body()
                    itemInformation
                    items.forEachIndexed { index, item ->
                        setItemInformation(item, index)
                    }
                    if (!EventBus.getDefault().isRegistered(this@CharacterGearViewModel)) {
                        EventBus.getDefault().register(this@CharacterGearViewModel)
                    }
                    itemsInfoSetup.value = true
                } else {
                    errorCode.value = response.code()
                }
            }
        }
        jobs.add(job)
    }

    private val itemInformation: Unit
        get() {
            items.add(itemsInformation.value!!.shoulders)
            items.add(itemsInformation.value!!.hands)
            items.add(itemsInformation.value!!.leftFinger)
            items.add(itemsInformation.value!!.mainHand)
            items.add(itemsInformation.value!!.head)
            items.add(itemsInformation.value!!.torso)
            items.add(itemsInformation.value!!.waist)
            items.add(itemsInformation.value!!.legs)
            items.add(itemsInformation.value!!.feet)
            items.add(itemsInformation.value!!.neck)
            items.add(itemsInformation.value!!.bracers)
            items.add(itemsInformation.value!!.rightFinger)
            items.add(itemsInformation.value!!.offHand)
        }

    private fun setItemInformation(item: Item, index: Int) {
        val primary = StringBuilder()
        val secondary = StringBuilder()
        val gem = StringBuilder()
        try {
            for (j in item.attributesHtml?.primary?.indices!!) {
                var attribute = item.attributesHtml?.primary!![j].replace("<span class=\"tooltip-icon-enchant\"></span>".toRegex(), "<img src=\"utility\">")
                attribute = attribute.replace("<span class=\"tooltip-icon-utility\"></span>".toRegex(), "<img src=\"utility\">")
                attribute = attribute.replace("<span class=\"tooltip-icon-bullet\"></span>".toRegex(), "<img src=\"primary" + "\">")
                attribute = attribute.replace("span class=\"d3-color-ff".toRegex(), "font color=\"#")
                attribute = attribute.replace("span class=\"d3-color-magic".toRegex(), "font color=\"#7979d4")
                primary.append(attribute.replace("</span>".toRegex(), "</font>")).append("<br>")
            }
        } catch (e: Exception) {
            primary.append("")
        }
        try {
            for (j in item.attributesHtml?.secondary?.indices!!) {
                var attribute = item.attributesHtml?.secondary!![j].replace("<span class=\"tooltip-icon-enchant\"></span>".toRegex(), "<img src=\"utility\">")
                attribute = attribute.replace("<span class=\"tooltip-icon-bullet\"></span>".toRegex(), "<img src=\"primary" +
                        "\">")
                attribute = attribute.replace("<span class=\"tooltip-icon-utility\"></span>".toRegex(), "<img src=\"utility\">")
                attribute = attribute.replace("span class=\"d3-color-ff".toRegex(), "font color=\"#")
                attribute = attribute.replace("span class=\"d3-color-magic".toRegex(), "font color=\"#7979d4")
                secondary.append(attribute.replace("</span>".toRegex(), "</font>")).append("<br>")
            }
        } catch (e: Exception) {
            secondary.append("")
        }
        try {
            for (j in item.gems?.indices!!) {
                val gemAttributes = StringBuilder()
                if (item.gems!![j].item.id.contains("Unique")) {
                    gemAttributes.append("<font color=\"#ff8000\"> ")
                }
                for (k in item.gems!![j].attributes.indices) {
                    gemAttributes.append(" <img src=\"").append(item.gems!![j].item.icon).append("\">")
                    gemAttributes.append(" <img src=\"primary\"> ")
                    gemAttributes.append(item.gems!![j].attributes[k].replace("\\n".toRegex(), "<br>")).append("<br>")
                }
                gemAttributes.append("</font>")
                gem.append(gemAttributes)
            }
            if (item.openSockets!! > 0) {
                for (i in 0 until item.openSockets?.toInt()!!) {
                    gem.append("<img src=\"empty_socket_d3\"> ").append("Empty Socket<br>")
                }
            }
        } catch (e: Exception) {
            gem.append("")
        }
        primaryStatsMap[index] = primary.toString()
        secondaryStatsMap[index] = secondary.toString()
        gemsMap[index] = gem.toString()
    }

    @Subscribe
    override fun localeSelectedReceived(LocaleSelectedEvent: LocaleSelectedEvent) {
        super.localeSelectedReceived(LocaleSelectedEvent)
        downloadItemInformation(battletag, id, selectedRegion)
    }
}