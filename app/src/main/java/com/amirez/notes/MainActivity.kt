package com.amirez.notes

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amirez.notes.adapter.NoteAdapter
import com.amirez.notes.database.AppDatabase
import com.amirez.notes.database.NoteDao
import com.amirez.notes.databinding.ActivityMainBinding
import com.amirez.notes.model.Note
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), NoteEvent {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var dao: NoteDao
    private lateinit var noteLayoutManager: RecyclerView.LayoutManager
    private lateinit var notesList: ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getInstance(this).noteDao

//        setupRecyclerView()
        setupToolbar()

        binding.fabAdd.setOnClickListener {
            Intent(this, AddNewNoteActivity::class.java).also {
                startActivity(it)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()// not the best way to refresh the adapter!
    }

    private fun getAllNotesOrShowAlternativeView() {

        runBlocking {
            launch(Dispatchers.Default) {
                notesList = ArrayList(dao.getAllNotes())
            }
        }

        if (notesList.isEmpty())
            binding.alternativeViewForEmptyDatabase.visibility = View.VISIBLE
        else
            binding.alternativeViewForEmptyDatabase.visibility = View.INVISIBLE
    }

    private fun setupRecyclerView() {
        getAllNotesOrShowAlternativeView()
        noteLayoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        noteAdapter = NoteAdapter(notesList, this)

        binding.rvMain.apply {
            adapter = noteAdapter
            layoutManager = noteLayoutManager
        }
    }

    private fun setupToolbar() {

        binding.toolbarMain.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_setting -> {
                    Toast.makeText(this, "Setting pressed.", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.mi_change_view -> {
                    Toast.makeText(this, "change view pressed.", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.mi_about -> {
                    Toast.makeText(this, "about pressed.", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
    }

    override fun onNoteClicked(note: Note) {
        Intent(this, DetailsActivity::class.java).also {
            it.putExtra(NOTE_KEY, note)
            startActivity(it)
        }
    }
}