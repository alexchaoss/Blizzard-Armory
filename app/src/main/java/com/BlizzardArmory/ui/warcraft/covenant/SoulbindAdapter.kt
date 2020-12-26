package com.BlizzardArmory.ui.warcraft.covenant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.model.warcraft.covenant.character.soulbind.Traits
import com.BlizzardArmory.model.warcraft.covenant.techtalent.TechTalentWithIcon
import com.BlizzardArmory.model.warcraft.covenant.techtalenttree.Talents

class SoulbindAdapter(private val list: List<List<Talents>>, private val traits: List<Traits>, private val icons: List<TechTalentWithIcon>, private val context: Context)
    : RecyclerView.Adapter<SoulbindViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoulbindViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SoulbindViewHolder(inflater, parent, context)
    }

    override fun onBindViewHolder(holder: SoulbindViewHolder, position: Int) {
        val talents: List<Talents> = list[position]
        holder.bind(talents, traits.find { it.tier == position }!!, icons)
    }

    override fun getItemCount(): Int = list.size

}