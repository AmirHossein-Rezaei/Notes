package com.amirez.notes

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amirez.notes.adapter.NoteAdapter
import com.amirez.notes.database.AppDatabase
import com.amirez.notes.database.NoteDao
import com.amirez.notes.databinding.ActivityMainBinding
import com.amirez.notes.model.Note
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        checkIfListIsEmpty()
    }

    private fun checkIfListIsEmpty() {
        if (notesList.isEmpty())
            binding.alternativeViewForEmptyDatabase.visibility = View.VISIBLE
        else
            binding.alternativeViewForEmptyDatabase.visibility = View.INVISIBLE
    }

    private fun deleteNote(note: Note, pos: Int) {
        runBlocking {
            launch(Dispatchers.Default) {
                dao.deleteNote(note)
            }
        }
        noteAdapter.itemDeleted(note, pos)
        notesList.remove(note)
        checkIfListIsEmpty()
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

    override fun onNoteLongClicked(note: Note, pos: Int) {

        MaterialAlertDialogBuilder(
            this,
            R.style.MyAlertDialogTheme
        ).setMessage("Do you want to delete this note?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteNote(note, pos)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .setIcon(ContextCompat.getDrawable(this, R.drawable.ic_delete))
            .show()
    }
}