package com.davinciapp.holmesclub.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.davinciapp.holmesclub.repository.DraftRepository

class DraftViewModel(draftRepo: DraftRepository) : ViewModel(){

    val draftItems: LiveData<List<DraftListItem>> = Transformations.map(draftRepo.getAllDrafts()) {drafts ->
        val items = arrayListOf<DraftListItem>()
        for (draft in drafts) {
            items.add( DraftListItem(draft.title, draft.modifTime.toString()))
        }
        return@map items
    }

}

data class DraftListItem(val title: String, val modifTime: String)