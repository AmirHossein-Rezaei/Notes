package com.amirez.notes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    var title: String,
    var content: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)