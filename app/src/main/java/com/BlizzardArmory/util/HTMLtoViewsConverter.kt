package com.BlizzardArmory.util

import android.content.Context
import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.util.regex.Matcher
import java.util.regex.Pattern


class HTMLtoViewsConverter(val context: Context) {

    val linearLayout = LinearLayout(context)

    init {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.setMargins(0, MetricConversion.getDPMetric(10, context), 0, MetricConversion.getDPMetric(5, context))
        linearLayout.layoutParams = params
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.tag = "content_layout"
    }

    fun parseHtml(source: String) {

        val pattern = Pattern.compile("<img.+?>|<iframe.+?>")
        val matcher = pattern.matcher(source)
        val splitSource = source.split("<img.+?>|<iframe.+?>".toRegex())
        val componentList = arrayListOf<Component>()

        if (splitSource.isEmpty() || splitSource.size == 1) {
            componentList.add(getComponent(1, source))
        } else {
            var index = 0
            while (matcher.find()) {
                componentList.add(getComponent(1, splitSource[index]))
                val patternImg = Pattern.compile("<img.+?>")
                val patternVid = Pattern.compile("<iframe.+?>")
                val matcherImg = patternImg.matcher(matcher.group())
                val matcherVid = patternVid.matcher(matcher.group())
                when {
                    matcherImg.find() -> {
                        componentList.add(getComponent(3, matcherImg.group()))
                    }
                    matcherVid.find() -> {
                        componentList.add(getComponent(4, matcherVid.group()))
                    }
                }
                index++
            }
        }
        parseComponents(componentList)
    }

    private fun parseComponents(componentList: ArrayList<Component>) {
        for ((i, component) in componentList.withIndex()) {
            when (component.type) {
                1 -> {
                    val textView = TextView(context)
                    textView.setTextColor(Color.parseColor("#888888"))
                    component.info =
                        component.info?.replace("<a href", "</font><font color=#2788b5><a href")
                    component.info = component.info?.replace("</a>", "</a></font>")
                    Log.i("TEST", component.info!!)
                    textView.text =
                        HtmlCompat.fromHtml(component.info!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    textView.movementMethod = LinkMovementMethod.getInstance()
                    linearLayout.addView(textView)
                }
                2 -> {
                    val textView = TextView(context)
                    textView.text = HtmlCompat.fromHtml(
                        "<font color=#2788b5>" + component.info!! + "</font>",
                        HtmlCompat.FROM_HTML_MODE_COMPACT
                    )
                    textView.movementMethod = LinkMovementMethod.getInstance()
                    linearLayout.addView(textView)
                }
                3 -> {
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    val relativeLayout = RelativeLayout(context)
                    relativeLayout.layoutParams = params
                    val imageView = ImageView(context)
                    imageView.adjustViewBounds = true
                    relativeLayout.addView(imageView, params)
                    val p =
                        Pattern.compile("https?.+?\\.png|https?.+?\\.jpg|https?.+?\\.jpeg|https?.+?\\.gif|https?.+?\\.svg|https?.+?\\.webp|https?.+?\\.ico|https?.+?\\.bmp")
                    val m: Matcher = p.matcher(component.info!!)
                    if (m.find()) {
                        Glide.with(context).load(m.group()).into(imageView)
                    }
                    linearLayout.addView(relativeLayout)
                }
                4 -> {
                    val youtubePlayerView = YouTubePlayerView(context)
                    youtubePlayerView.tag = "youtube_view${i}"
                    youtubePlayerView.getPlayerUiController().showFullscreenButton(true)
                    youtubePlayerView.addYouTubePlayerListener(object :
                        AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            var videoId = ""
                            val p = Pattern.compile("embed/(\\w+)")
                            val m: Matcher = p.matcher(component.info!!)
                            if (m.find()) {
                                videoId = m.group(1)!!
                            }
                            youTubePlayer.cueVideo(videoId, 0f)
                        }
                    })
                    linearLayout.addView(youtubePlayerView)
                }
            }
        }
    }

    private fun getComponent(type: Int, info: String): Component {
        val component = Component()
        component.type = type
        component.info = info
        return component
    }
}

internal class Component {
    var type = 0
    var info: String? = null
}