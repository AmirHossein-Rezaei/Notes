package com.amirez.notes

import com.amirez.notes.model.Note

interface NoteEvent {
    fun insertNote(note: Note)
}