package com.BlizzardArmory.ui.warcraft.account

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.R
import com.BlizzardArmory.model.warcraft.favorite.FavoriteCharacter
import com.BlizzardArmory.model.warcraft.media.Media
import com.BlizzardArmory.network.RetroClient
import com.BlizzardArmory.network.URLConstants
import com.BlizzardArmory.network.oauth.BattlenetOAuth2Helper
import com.BlizzardArmory.network.oauth.BattlenetOAuth2Params
import com.BlizzardArmory.ui.main.MainActivity
import com.BlizzardArmory.ui.warcraft.navigation.WoWNavFragment
import com.BlizzardArmory.util.ConnectionStatus
import com.BlizzardArmory.util.events.NetworkEvent
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


class FavoritesViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val context: Context) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.wow_character_list, parent, false)) {

    private var avatar: ImageView? = null
    private var faction: ImageView? = null
    private var name: TextView? = null
    private var characterClass: TextView? = null
    private var level: TextView? = null
    private var characterLayout: CardView? = null
    private var gson: Gson? = null
    private var battlenetOAuth2Params: BattlenetOAuth2Params? = null
    private var fragmentManager: FragmentManager? = null
    private var character: FavoriteCharacter? = null
    private var downloaded = false
    private var job: Job? = null

    init {
        avatar = itemView.findViewById(R.id.avatar)
        faction = itemView.findViewById(R.id.faction)
        name = itemView.findViewById(R.id.name)
        characterClass = itemView.findViewById(R.id.character_class)
        level = itemView.findViewById(R.id.level)
        characterLayout = itemView.findViewById(R.id.character_layout)
        gson = GsonBuilder().create()
        EventBus.getDefault().register(this)
        job = CoroutineScope(Dispatchers.Default).launch {
            do {
                delay(3000)
                if (!downloaded) {
                    EventBus.getDefault().post(NetworkEvent(ConnectionStatus.hasNetwork()))
                }
            } while (!ConnectionStatus.hasNetwork())
        }
    }


    fun bind(character: FavoriteCharacter, battlenetOAuth2Params: BattlenetOAuth2Params, fragmentManager: FragmentManager) {
        this.battlenetOAuth2Params = battlenetOAuth2Params
        this.fragmentManager = fragmentManager
        this.character = character
        name?.text = character.characterSummary?.name
        characterClass?.text = character.characterSummary?.characterClass?.name
        level?.text = "Level " + character.characterSummary?.level
        if (character.characterSummary?.faction?.type == "HORDE") {
            faction?.setBackgroundResource(R.drawable.horde_logo)
        } else {
            faction?.setBackgroundResource(R.drawable.alliance_logo)
        }
        downloadMedia()
    }

    private fun downloadMedia() {
        val bnOAuth2Helper = BattlenetOAuth2Helper(battlenetOAuth2Params!!)
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetroClient.getWoWClient().getMedia(
                character?.characterSummary?.name?.toLowerCase(Locale.ROOT),
                character?.characterSummary?.realm?.slug,
                MainActivity.locale,
                character?.region?.toLowerCase(Locale.ROOT),
                bnOAuth2Helper.accessToken
            )
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val media = response.body()!!
                    onClickCharacter(gson?.toJson(response.body())!!, fragmentManager!!)
                    downloadAvatar(media)
                } else {
                    Log.e("Error", "Code: ${response.code()} Message: ${response.message()}")
                    onClickCharacter(gson?.toJson(response.body())!!, fragmentManager!!)
                }
            }
        }
    }

    private fun downloadAvatar(media: Media?) {
        val mediaUrl: String? = if (media == null) {
            "https://render-us.worldofwarcraft.com/character/auchindoun/0/0-main.jpg"
        } else {
            media.assets?.first { it.key == "avatar" }?.value
        }
        val fullURL = mediaUrl + URLConstants.NOT_FOUND_URL_AVATAR + character?.characterSummary?.characterClass?.id + "-" + (if (character?.characterSummary?.gender?.type == "MALE") 1 else 0) + ".jpg"
        Glide.with(context).load(fullURL).placeholder(R.drawable.loading_placeholder).into(avatar!!)
        downloaded = true
        job?.cancel()
    }

    private fun onClickCharacter(media: String, fragmentManager: FragmentManager) {
        characterLayout?.setOnClickListener {
            val woWNavFragment = WoWNavFragment.newInstance(character?.characterSummary?.name?.toLowerCase(Locale.ROOT)!!, character?.characterSummary?.realm?.slug!!, media, character?.region!!)
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
            fragmentTransaction.replace(R.id.fragment, woWNavFragment, "NAV_FRAGMENT")
            fragmentTransaction.addToBackStack("wow_nav").commit()
            fragmentManager.executePendingTransactions()
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun networkEventReceived(networkEvent: NetworkEvent) {
        if (networkEvent.data) {
            downloadMedia()
        }
    }

}
