package com.amirez.notes.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notes_table")
data class Note(
    var title: String?,
    var content: String?,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
):Parcelable