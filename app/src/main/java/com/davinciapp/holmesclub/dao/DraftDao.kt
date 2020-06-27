package com.davinciapp.holmesclub.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.davinciapp.holmesclub.view.drafts.model.Draft

@Dao
interface DraftDao {

    //READ
    @Query("SELECT * FROM draft")
    fun getAll(): LiveData<List<Draft>>

    @Query("SELECT * FROM draft WHERE id = :id")
    suspend fun get(id: Int): Draft

    //INSERT
    @Insert
    suspend fun insert(draft: Draft): Long

    //UPDATE
    @Update
    suspend fun update(draft: Draft)

    @Query("UPDATE draft SET picture_uri = :uri WHERE id = :id")
    suspend fun updateCoverPictureUri(id: Int, uri: String)

    @Query("""
        UPDATE draft 
        SET title = :title, content = :content, words_nbr = :words, modif_time = :time
        WHERE id = :id""")
    suspend fun updateDraftContent(id: Int, title: String, content: String, words: Int, time: Long)

    //DELETE
    @Query("DELETE FROM draft WHERE id = :draftId")
    suspend fun delete(draftId: Int)
}