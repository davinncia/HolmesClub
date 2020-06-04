package com.davinciapp.holmesclub.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davinciapp.holmesclub.drafts.DraftViewModel
import com.davinciapp.holmesclub.editor.EditorViewModel
import com.davinciapp.holmesclub.json.JsonParser
import com.davinciapp.holmesclub.repository.DraftRepository

class ViewModelFactory private constructor(private val application: Application): ViewModelProvider.Factory {

    private val draftRepo = DraftRepository.getInstance(application)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(EditorViewModel::class.java) -> {
                EditorViewModel(JsonParser(), draftRepo) as T
            }
            modelClass.isAssignableFrom(DraftViewModel::class.java) -> {
                DraftViewModel(draftRepo) as T
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