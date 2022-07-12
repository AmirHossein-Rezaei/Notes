package com.amirez.notes.database

import androidx.room.*
import com.amirez.notes.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT * FROM notes_table")
    fun getAllNotes() : List<Note>

    @Query("SELECT * FROM notes_table WHERE id = :id")
    fun getNoteById(id: Int): Note

}