package com.BlizzardArmory.util

import com.BlizzardArmory.model.news.NewsMetaData
import com.BlizzardArmory.model.news.NewsPage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object WebNewsScrapper {

    val newsList = arrayListOf<NewsMetaData>()

    fun parseNewsList(pageURL: String) {
        newsList.clear()
        Jsoup.connect(pageURL).ignoreContentType(true).get().run {
            extractHtml()
        }
    }

    fun parseMoreNews(pageURL: String) {
        Jsoup.connect(pageURL).ignoreContentType(true).get().run {
            var html = this.select("body").html()
                    .replace("\\&quot;", "")
                    .replace("flush-top\"", "flush-top")
                    .replace("articlelistitem-labelinner\\", "Articlelistitem-labelinner")
                    .replace("""timestamp="[a-zA-Z]{3}"""".toRegex()) { it.value.substring(0, it.value.length - 1) }
                    .replace("""timestamp="[a-zA-Z]{3}.+\\""".toRegex()) { it.value.substring(0, it.value.length - 1) }
            html = html.substring(10, html.length - 3)
            Jsoup.parse(html).run {
                extractHtml()
            }
            newsList.forEach {
            }
        }
    }

    private fun Document.extractHtml() {
        select(".ArticleListItem").forEachIndexed { index, element ->
            val url = "https://news.blizzard.com${element.select(".ArticleLink").attr("href")}"
            val title = element.select(".sr-only").text()
            val game = element.select(".ArticleListItem-labelInner").text()
            val description = element.select("div.ArticleListItem-description > div.h6").text()
            val time = element.select(".ArticleListItem-footerTimeStamp").text()
            var imageURL = element.select(".ArticleListItem-image").attr("style")
            val timestamp = element.select(".ArticleListItem-footerTimestamp").attr("timestamp")
            imageURL = "https:${imageURL.substring(21, imageURL.length - 1)}"
            newsList.add(NewsMetaData(url, game, imageURL, title, description, time, timestamp))
        }
    }

    fun parseNewsPage(pageURL: String): NewsPage {
        Jsoup.connect(pageURL).get().run {
            select("#article-detail").forEachIndexed { index, element ->
                val game = element.select(".ArticleDetail-community").text()
                val title = element.select(".ArticleDetail-title").text()
                val author = element.select(".ArticleDetail-bylineAuthor").text()
                val date = element.select(".ArticleDetail-bylineDate").text()
                val imageURL = "https:${element.select(".Image-image").attr("src")}"
                val content = element.select(".ArticleDetail-Content").html()

                return NewsPage(game, title, author, date, imageURL, content)
            }
        }
        return NewsPage("", "", "", "", "", "")
    }
}