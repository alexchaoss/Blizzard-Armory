package com.BlizzardArmory.ui.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.BlizzardArmory.R
import com.BlizzardArmory.util.events.LoadNewsEvent
import com.BlizzardArmory.util.events.MoreNewsClickEvent
import com.github.ybq.android.spinkit.SpinKitView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewsShowMoreViewHolder(inflater: LayoutInflater, parent: ViewGroup, val context: Context) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.news_show_more_item, parent, false)) {

    private var showMore: Button? = null
    private var loading: SpinKitView? = null


    init {
        showMore = itemView.findViewById(R.id.show_more)
        loading = itemView.findViewById(R.id.loading)
        EventBus.getDefault().register(this)
    }

    fun bind() {
        showMore?.setOnClickListener {
            showMore?.visibility = View.GONE
            loading?.visibility = View.VISIBLE
            EventBus.getDefault().post(MoreNewsClickEvent())
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public fun loadNewsEventReceived(loadNewsEvent: LoadNewsEvent) {
        showMore?.visibility = View.VISIBLE
        loading?.visibility = View.GONE
    }
}