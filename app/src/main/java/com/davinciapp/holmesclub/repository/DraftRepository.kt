package com.davinciapp.holmesclub.repository

import android.content.Context
import com.davinciapp.holmesclub.dao.DraftDao
import com.davinciapp.holmesclub.db.AppDatabase
import com.davinciapp.holmesclub.view.drafts.model.Draft

class DraftRepository(context: Context) {

    private val draftDao: DraftDao

    init {
        val db = AppDatabase.getDatabase(context)
        draftDao = db.draftDao()
    }

    fun getAllDrafts() = draftDao.getAll()
    suspend fun getDraft(id: Int) = draftDao.get(id)

    suspend fun insert(draft: Draft): Long =
        draftDao.insert(draft)

    suspend fun update(draft: Draft) = draftDao.update(draft)
    suspend fun updateCoverPictureUri(id: Int, uri: String) = draftDao.updateCoverPictureUri(id, uri)
    suspend fun updateDraftContent(id: Int, title: String, content: String, words: Int, currentTime: Long) =
        draftDao.updateDraftContent(id, title, content, words, currentTime)

    suspend fun delete(draftId: Int) =
        draftDao.delete(draftId)

    companion object {
        private var INSTANCE: DraftRepository? = null

        fun getInstance(context: Context): DraftRepository {
            if (INSTANCE == null) {
                synchronized(DraftRepository) {
                    if (INSTANCE == null) {
                        INSTANCE = DraftRepository(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }
}