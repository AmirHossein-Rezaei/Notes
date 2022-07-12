package com.amirez.notes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.amirez.notes.adapter.NoteAdapter
import com.amirez.notes.database.AppDatabase
import com.amirez.notes.database.NoteDao
import com.amirez.notes.databinding.ActivityAddNewNoteBinding
import com.amirez.notes.model.Note
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class AddNewNoteActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewNoteBinding
    private lateinit var dao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = AppDatabase.getInstance(this).noteDao
        setupToolbar()

    }

    private fun storeData() {
        val title = binding.etTitleAdd.text.toString()
        val content = binding.etContentAdd.text.toString()
        val note = Note(title, content)

        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                onBackPressed()
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbarAdd.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_setting -> {
                    Toast.makeText(this, "Setting pressed.", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.mi_done -> {
                    storeData()
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

}