package com.BlizzardArmory.ui.warcraft.covenant

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.BlizzardArmory.model.warcraft.covenant.character.soulbind.SoulbindInformation
import com.BlizzardArmory.model.warcraft.covenant.covenant.custom.CovenantSpells
import com.BlizzardArmory.model.warcraft.covenant.techtalent.TechTalentWithIcon
import com.BlizzardArmory.model.warcraft.covenant.techtalenttree.TechTalentTree
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.network.URLConstants
import com.BlizzardArmory.ui.BaseViewModel
import com.BlizzardArmory.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.util.*

class CovenantViewModel : BaseViewModel() {

    lateinit var character: String
    lateinit var realm: String
    lateinit var region: String

    private val soulbinds: MutableLiveData<SoulbindInformation> = MutableLiveData()
    private val techTalents: MutableLiveData<MutableMap<Int, List<TechTalentWithIcon>>> = MutableLiveData()
    private val techTrees: MutableLiveData<MutableMap<Int, TechTalentTree>> = MutableLiveData()
    private var covenantClassSpells: MutableLiveData<List<CovenantSpells>> = MutableLiveData()
    private var covenantSpell: MutableLiveData<List<CovenantSpells>> = MutableLiveData()

    private val talentMap = mutableMapOf<Int, List<TechTalentWithIcon>>()
    private val treeMap = mutableMapOf<Int, TechTalentTree>()

    init {
        techTalents.value = mutableMapOf()
    }

    fun getSoulbinds(): LiveData<SoulbindInformation> {
        return soulbinds
    }

    fun getTechTalents(): LiveData<MutableMap<Int, List<TechTalentWithIcon>>> {
        return techTalents
    }

    fun getTechTrees(): LiveData<MutableMap<Int, TechTalentTree>> {
        return techTrees
    }

    fun getcovenantClassSpells(): LiveData<List<CovenantSpells>> {
        return covenantClassSpells
    }

    fun getcovenantSpell(): LiveData<List<CovenantSpells>> {
        return covenantSpell
    }

    fun downloadCharacterSoulbinds() {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getSoulbinds(
                character.toLowerCase(Locale.ROOT),
                realm.toLowerCase(Locale.ROOT),
                MainActivity.locale,
                region.toLowerCase(Locale.ROOT),
                battlenetOAuth2Helper!!.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    soulbinds.value = response.body()
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                }
            }
        }
        jobs.add(job)
    }

    fun downloadTechTree(id: Int, soulbindId: Int) {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient()
                .getTechTree(id, MainActivity.locale, region.toLowerCase(Locale.ROOT))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    treeMap[soulbindId] = response.body()!!
                    techTrees.value = treeMap
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                }
            }
            if (!EventBus.getDefault().isRegistered(this@CovenantViewModel)) {
                EventBus.getDefault().register(this@CovenantViewModel)
            }
        }
        jobs.add(job)
    }

    fun downloadCovenantClassSpell(characterClass: Int) {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getCovenantSpells(
                URLConstants.getCovenantClassSpells(
                    characterClass,
                    MainActivity.locale
                )
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    covenantClassSpells.value = response.body()
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                }
            }
        }
        jobs.add(job)
    }

    fun downloadCovenantSpell(covenantId: Int) {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient()
                .getCovenantSpells(URLConstants.getCovenantSpells(covenantId, MainActivity.locale))
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    covenantSpell.value = response.body()
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                }
            }
        }
        jobs.add(job)
    }

    fun downloadTechTalents(soulbindId: Int) {
        val job = coroutineScope.launch {
            val response = RetroClient.getWoWClient().getTechTalentsWithIcon(
                URLConstants.getTechTalents(
                    soulbindId,
                    MainActivity.locale
                )
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    talentMap[soulbindId] = response.body()!!
                    techTalents.value = talentMap
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                }
            }
        }
        jobs.add(job)
    }
}