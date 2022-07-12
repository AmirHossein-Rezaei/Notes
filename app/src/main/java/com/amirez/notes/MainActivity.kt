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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), NoteEvent {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var dao: NoteDao
    private lateinit var noteLayoutManager: RecyclerView.LayoutManager
    private lateinit var notesList: ArrayList<Note>

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = AppDatabase.getInstance(this).noteDao

        setupRecyclerView()
        setupToolbar()

        binding.fabAdd.setOnClickListener {
            Intent(this, AddNewNoteActivity::class.java).also {
                startActivity(it)
            }
        }

    }

    private suspend fun getAllNotes() {
        lifecycleScope.launch(Dispatchers.Default) {
            notesList = ArrayList(dao.getAllNotes())
        }.join()
        if (notesList.isEmpty())
            showAlternativeView()
    }

    private fun showAlternativeView() {
        binding.alternativeViewForEmptyDatabase.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        lifecycleScope.launch {
            withContext(Dispatchers.Default){
                getAllNotes()
            }
            withContext(Dispatchers.Main) {
                noteLayoutManager =
                    GridLayoutManager(this@MainActivity, 2, GridLayoutManager.VERTICAL, false)
                noteAdapter = NoteAdapter(
                    notesList,
                    this@MainActivity
                )
                binding.rvMain.apply {
                    adapter = noteAdapter
                    layoutManager = noteLayoutManager
                }
            }
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

    override fun insertNote(note: Note) {
        TODO("Not yet implemented")
    }
}