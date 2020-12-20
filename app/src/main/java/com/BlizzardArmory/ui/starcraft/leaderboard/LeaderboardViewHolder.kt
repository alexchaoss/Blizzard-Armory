package com.BlizzardArmory.ui.starcraft.leaderboard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.R
import com.BlizzardArmory.model.starcraft.leaderboard.LadderMembers


class LeaderboardViewHolder(inflater: LayoutInflater, parent: ViewGroup, private val context: Context) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.sc2_leaderboard_list, parent, false)) {

    var rank: TextView? = null
    var points: TextView? = null
    var win: TextView? = null
    var losses: TextView? = null
    var players: RecyclerView? = null

    init {
        rank = itemView.findViewById(R.id.rank)
        points = itemView.findViewById(R.id.points)
        win = itemView.findViewById(R.id.win)
        losses = itemView.findViewById(R.id.losses)
        players = itemView.findViewById(R.id.players_recycler)
    }

    fun bind(team: List<LadderMembers>, position: Int) {
        rank?.text = position.toString()
        when {
            team[0].previousRank == position -> {
                rank?.text = "-    ${position}"
                rank?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
            team[0].previousRank == 0 -> {
                rank?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.exclamation, 0, 0, 0)
            }
            team[0].previousRank > position -> rank?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_up, 0, 0, 0)
            team[0].previousRank < position -> rank?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.arrow_down, 0, 0, 0)
        }

        points?.text = team[0].points.toString()
        win?.text = team[0].wins.toString()
        losses?.text = team[0].losses.toString()

        players?.apply {
            adapter = PlayerAdapter(team, context)
        }
    }
}
