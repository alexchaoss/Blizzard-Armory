package com.BlizzardArmory.ui.starcraft.leaderboard

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.model.starcraft.leaderboard.LadderMembers
import java.util.*

class LeaderboardAdapter(private val list: List<List<LadderMembers>>, private val context: Context, private var playerRank: Int)
    : RecyclerView.Adapter<LeaderboardViewHolder>() {

    private var fullLeaderboardList = ArrayList<List<LadderMembers>>()

    init {
        fullLeaderboardList.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LeaderboardViewHolder(inflater, parent, context)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val team: List<LadderMembers> = fullLeaderboardList[position]
        holder.bind(team, playerRank + position)
    }

    override fun getItemCount(): Int = fullLeaderboardList.size

    fun filter(constraint: String) {
        Log.i("Filter", constraint)
        fullLeaderboardList.clear()
        if (constraint.toLowerCase(Locale.ROOT).isEmpty()) {
            fullLeaderboardList.addAll(list)
        } else {
            if (constraint.toIntOrNull() != null && constraint.toInt() < list.size - 1) {
                fullLeaderboardList.add(list[constraint.toInt() - 1])
            } else {
                searchWithConstraint(constraint) { data, const ->
                    return@searchWithConstraint data.contains(const)
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun searchWithConstraint(constraint: String, search: (data: String, constraint: String) -> Boolean) {
        for (team in list) {
            var match = false
            for (member in team) {
                when {
                    search(member.character.displayName, constraint) -> {
                        match = true
                    }
                    search(member.character.clanName, constraint) -> {
                        match = true
                    }
                    search(member.character.clanTag, constraint) -> {
                        match = true
                    }
                }
            }

            if (match) {
                fullLeaderboardList.add(team)
            }
        }
    }
}