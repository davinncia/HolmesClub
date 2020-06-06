package com.davinciapp.holmesclub.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.davinciapp.holmesclub.drafts.model.Draft

@Dao
interface DraftDao {

    @Query("SELECT * FROM draft")
    fun getAll(): LiveData<List<Draft>>

    @Query("SELECT * FROM draft WHERE id = :id")
    suspend fun get(id: Int): Draft

    @Insert
    suspend fun insert(draft: Draft)

    @Update
    suspend fun update(draft: Draft)

    @Query("DELETE FROM draft WHERE id = :draftId")
    suspend fun delete(draftId: Int)
}