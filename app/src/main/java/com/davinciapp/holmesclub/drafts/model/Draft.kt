package com.davinciapp.holmesclub.drafts.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Draft(
    val title: String,
    val content: String,
    @ColumnInfo(name = "modif_time")
    val modifTime: Long
) {
    //Let room handle the Id
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}