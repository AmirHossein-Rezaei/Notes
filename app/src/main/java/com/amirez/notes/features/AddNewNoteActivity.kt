package com.amirez.notes.features

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.amirez.notes.R
import com.amirez.notes.database.AppDatabase
import com.amirez.notes.database.NoteDao
import com.amirez.notes.databinding.ActivityAddNewNoteBinding
import com.amirez.notes.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AddNewNoteActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewNoteBinding
    private lateinit var dao: NoteDao

    private var title: String? = null
    private var content: String? = null
    private var note: Note? = null

    private var isSavedInRecords = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getInstance(this).noteDao

        setupToolbar()
    }

    private fun insertOrUpdateData() {
        // checks if the item is already inserted or not
        // if not then insert it, and if it is then update it
        title = binding.layoutEditText.etTitle.text.toString()
        content = binding.layoutEditText.etContent.text.toString()

        if (!isSavedInRecords) {
            //insert new note
            note = Note(title, content)
            runBlocking {
                launch(Dispatchers.Default) {
                    dao.insertNote(note!!)
                    note = dao.getTheLastNote()
                }
            }
            isSavedInRecords = true
        } else {
            //update existing note
            note?.title = title
            note?.content = content
            runBlocking {
                launch(Dispatchers.Default) {
                    dao.updateNote(note!!)
                }
            }
        }

        Toast.makeText(this, "Note saved successfully.", Toast.LENGTH_SHORT).show()
    }

    private fun entriesAreSameAsLastSave(): Boolean {
        return binding.layoutEditText.etTitle.text.toString() == title
                && binding.layoutEditText.etContent.text.toString() == content
    }


    private fun entriesAreEmpty(): Boolean {

        return if (binding.layoutEditText.etTitle.text.toString().trim { it <= ' ' }.isEmpty() &&
            binding.layoutEditText.etContent.text.toString().trim { it <= ' ' }.isEmpty()
        ) {
            Toast.makeText(this, "Title and description can not both be empty!", Toast.LENGTH_SHORT)
                .show()
            true
        } else
            false

    }

    private fun setupToolbar() {
        binding.toolbarAdd.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_done -> {
                    if (!entriesAreEmpty() && !entriesAreSameAsLastSave())
                        insertOrUpdateData()
                    true
                }
                else -> false
            }
        }
        binding.toolbarAdd.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}