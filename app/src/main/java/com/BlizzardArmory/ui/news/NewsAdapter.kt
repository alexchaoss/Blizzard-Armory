package com.BlizzardArmory.ui.news

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.model.news.NewsMetaData

class NewsAdapter(private val list: MutableList<NewsMetaData>, val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (list[position].title.isBlank()) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            NewsViewHolder(inflater, parent, context)
        } else {
            NewsShowMoreViewHolder(inflater, parent, context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val news: NewsMetaData = list[position]
        if (holder.itemViewType == 0) {
            (holder as NewsViewHolder).bind(news)
        } else {
            (holder as NewsShowMoreViewHolder).bind()
        }
    }

    override fun getItemCount(): Int = list.size


    fun addItems(newList: List<NewsMetaData>) {
        val oldSize = list.size
        list.clear()
        list.addAll(newList)
        notifyItemRangeInserted(oldSize, list.size - oldSize)
    }
}