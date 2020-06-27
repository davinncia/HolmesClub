package com.davinciapp.holmesclub.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davinciapp.holmesclub.json.JsonParser
import com.davinciapp.holmesclub.repository.DraftRepository
import com.davinciapp.holmesclub.view.article.ArticleViewModel
import com.davinciapp.holmesclub.view.drafts.DraftViewModel
import com.davinciapp.holmesclub.view.editor.EditorViewModel
import com.davinciapp.holmesclub.view.feed.FeedViewModel
import com.davinciapp.holmesclub.view.login.EmailViewModel
import com.davinciapp.holmesclub.view.login.LoginViewModel
import com.davinciapp.holmesclub.view.login.WelcomeViewModel
import com.google.firebase.auth.FirebaseAuth

class ViewModelFactory private constructor(private val application: Application): ViewModelProvider.Factory {

    private val draftRepo = DraftRepository.getInstance(application)
    private val firebaseAuth = FirebaseAuth.getInstance()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EditorViewModel::class.java) -> {
                EditorViewModel(application, JsonParser(), draftRepo) as T
            }
            modelClass.isAssignableFrom(DraftViewModel::class.java) -> {
                DraftViewModel(draftRepo) as T
            }
            modelClass.isAssignableFrom(FeedViewModel::class.java) -> {
                FeedViewModel(firebaseAuth) as T
            }
            modelClass.isAssignableFrom(ArticleViewModel::class.java) -> {
                ArticleViewModel() as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel() as T
            }
            modelClass.isAssignableFrom(WelcomeViewModel::class.java) -> {
                WelcomeViewModel() as T
            }
            modelClass.isAssignableFrom(EmailViewModel::class.java) -> {
                EmailViewModel(firebaseAuth) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    companion object {
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(application)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}