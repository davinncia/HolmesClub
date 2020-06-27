package com.davinciapp.holmesclub.view.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class FeedViewModel(auth: FirebaseAuth) : ViewModel() {

    private var firebaseUser = auth.currentUser

    //LIVE EVENTS
    val notLoggedIn = MutableLiveData<Boolean>()
    val emailVerified = MutableLiveData<Boolean>()

    private val dummyArticleItem = listOf(
        ArticleFeedItem(1,"", "Article title", "Mike Demargo", 2876287628L),
        ArticleFeedItem(2,"", "Article title", "Mike Demargo", 2876287628L)
    )

    init {
        //Be sure to have an active user
        if (firebaseUser == null) notLoggedIn.value = true

        //Check email validation
        emailVerified.value = firebaseUser!!.isEmailVerified
    }

    val articles = MutableLiveData(dummyArticleItem)


    //Check email validation
    private fun checkEmailIsVerified() {

    }


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