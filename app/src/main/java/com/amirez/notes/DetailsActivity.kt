package com.amirez.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.amirez.notes.database.AppDatabase
import com.amirez.notes.database.NoteDao
import com.amirez.notes.databinding.ActivityDetailsBinding
import com.amirez.notes.model.Note

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var note: Note
    private lateinit var dao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getInstance(this).noteDao

        note = intent.getParcelableExtra(NOTE_KEY)!!

        setupViewsWithGivenNote()
    }

    private fun setupViewsWithGivenNote() {
        binding.etTitleUpdate.setText(note.title)
        binding.etContentUpdate.setText(note.content)
    }


}