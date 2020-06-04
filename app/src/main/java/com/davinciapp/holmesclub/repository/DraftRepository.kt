package com.davinciapp.holmesclub.repository

import android.content.Context
import com.davinciapp.holmesclub.dao.DraftDao
import com.davinciapp.holmesclub.db.AppDatabase
import com.davinciapp.holmesclub.drafts.model.Draft

class DraftRepository(context: Context) {

    private val draftDao: DraftDao

    init {
        val db = AppDatabase.getDatabase(context)
        draftDao = db.draftDao()
    }

    fun getAllDrafts() = draftDao.getAll()

    suspend fun insert(draft: Draft) = draftDao.insert(draft)

    suspend fun delete(draft: Draft) = draftDao.delete(draft)

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