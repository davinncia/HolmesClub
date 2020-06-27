package com.davinciapp.holmesclub.view.drafts

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davinciapp.holmesclub.repository.DraftRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DraftViewModel(private val draftRepo: DraftRepository) : ViewModel() {

    val draftItems: LiveData<List<DraftListItem>> =
        Transformations.map(draftRepo.getAllDrafts()) { drafts ->
            val items = arrayListOf<DraftListItem>()
            for (draft in drafts) {
                items.add(DraftListItem(draft.id, draft.title, draft.pictureUri, "${draft.wordsNbr} words", getDateStr(draft.modifTime)))
            }
            return@map items
        }

    fun removeDraft(draftId: Int) {
        viewModelScope.launch {
            draftRepo.delete(draftId)
        }
    }

    private fun getDateStr(timeStamp: Long): String {

        val dateFormat =
            if (timeStamp + (60_000 * 60 * 24) > System.currentTimeMillis()) {
                //Same day
                SimpleDateFormat("HH:mm", Locale.getDefault())
            } else {
                SimpleDateFormat("dd-MM HH:mm", Locale.getDefault())
            }

        return dateFormat.format(timeStamp)
    }

    data class DraftListItem(val id: Int, val title: String, val pictureCoverUri: String, val wordsNbr: String, val modifTime: String)

}

