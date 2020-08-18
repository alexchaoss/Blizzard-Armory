package com.BlizzardArmory.ui.ui_warcraft.achievements

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.model.warcraft.achievements.DetailedAchievement
import com.BlizzardArmory.model.warcraft.achievements.categories.Category

class CategoriesAdapter(private val list: List<Category>, private val locale: String, private val faction: String, private val mappedAchievements: Map<Long, List<DetailedAchievement>>)
    : RecyclerView.Adapter<CategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoriesViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category: Category = list[position]
        holder.bind(category, locale, faction, mappedAchievements)
    }

    override fun getItemCount(): Int = list.size

}