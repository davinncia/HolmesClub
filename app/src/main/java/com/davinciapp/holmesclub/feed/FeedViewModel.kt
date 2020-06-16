package com.davinciapp.holmesclub.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FeedViewModel() : ViewModel() {

    private val dummyArticleItem = listOf(
        ArticleFeedItem(1,"", "Article title", "Mike Demargo", 2876287628L),
        ArticleFeedItem(2,"", "Article title", "Mike Demargo", 2876287628L)
    )

    val articles = MutableLiveData(dummyArticleItem)



    data class ArticleFeedItem(
        val viewType: Int,
        val coverPictureUri: String,
        val title: String,
        val author: String,
        val creationTime: Long
    ) {
        companion object {
            const val VIEW_ONE = 1
            const val VIEW_TWO = 2
        }
    }
}