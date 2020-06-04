package com.davinciapp.holmesclub.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.davinciapp.holmesclub.drafts.model.Draft

@Dao
interface DraftDao {

    @Query("SELECT * FROM draft")
    fun getAll(): LiveData<List<Draft>>

    @Query("SELECT * FROM draft WHERE id = :id")
    fun get(id: Int): LiveData<Draft>

    @Insert
    suspend fun insert(draft: Draft)

    @Delete
    suspend fun delete(draft: Draft)
}