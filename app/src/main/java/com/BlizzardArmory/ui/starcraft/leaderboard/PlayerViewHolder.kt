package com.BlizzardArmory.ui.starcraft.leaderboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.R
import com.BlizzardArmory.model.starcraft.leaderboard.Character
import com.BlizzardArmory.model.starcraft.leaderboard.LadderMembers
import com.BlizzardArmory.ui.navigation.GamesActivity
import com.BlizzardArmory.ui.starcraft.profile.SC2Fragment


class PlayerViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val context: Context) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.d3_player_list, parent, false)) {

    var name: TextView? = null
    var icon: ImageView? = null

    init {
        name = itemView.findViewById(R.id.btag)
        icon = itemView.findViewById(R.id.icon)
    }

    fun bind(player: LadderMembers) {
        name?.text = player.character.displayName
        icon?.visibility = View.GONE
        onClickCharacter(player.character)
    }

    private fun onClickCharacter(player: Character) {
        itemView.setOnClickListener {
            val fragment: Fragment = SC2Fragment()
            val bundle = Bundle()
            bundle.putInt("regionId", player.region)
            bundle.putInt("realmId", player.realm)
            bundle.putString("profileId", player.id.toString())
            fragment.arguments = bundle
            (context as GamesActivity).supportFragmentManager.beginTransaction().add(R.id.fragment, fragment, "sc2fragment").addToBackStack("sc2").commit()
            context.supportFragmentManager.executePendingTransactions()
        }
    }

}
