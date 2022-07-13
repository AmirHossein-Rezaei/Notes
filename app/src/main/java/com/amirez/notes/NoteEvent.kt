package com.amirez.notes

import com.amirez.notes.model.Note

interface NoteEvent {
    fun onNoteClicked(note: Note)
    fun onNoteLongClicked(note: Note, pos: Int)
}