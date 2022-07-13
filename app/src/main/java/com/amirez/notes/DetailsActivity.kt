package com.amirez.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.amirez.notes.database.AppDatabase
import com.amirez.notes.database.NoteDao
import com.amirez.notes.databinding.ActivityDetailsBinding
import com.amirez.notes.model.Note
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var note: Note
    private lateinit var dao: NoteDao

    private var title: String? = null
    private var content: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = AppDatabase.getInstance(this).noteDao

        note = intent.getParcelableExtra(NOTE_KEY)!!
        title = note.title
        content = note.content

        setupViewsWithGivenNote()
        setupToolbar()
    }

    private fun setupViewsWithGivenNote() {
        binding.etTitleUpdate.setText(note.title)
        binding.etContentUpdate.setText(note.content)
    }

    private fun entriesAreEmpty(): Boolean {
        return if (binding.etTitleUpdate.text.toString().trim { it <= ' ' }.isEmpty() &&
            binding.etContentUpdate.text.toString().trim { it <= ' ' }.isEmpty()
        ) {
            Toast.makeText(this, "Title and description can not both be empty!", Toast.LENGTH_SHORT)
                .show()
            true
        } else
            false
    }

    private fun updateNote() {
        // save title and content in a variable so the
        // entriesAreSameAsLastSave() function would work
        title = binding.etTitleUpdate.text.toString()
        content = binding.etContentUpdate.text.toString()

        note.title = title
        note.content = content
        runBlocking {
            launch(Dispatchers.Default) {
                dao.updateNote(note)
            }
        }
        Toast.makeText(this, "Note updated.", Toast.LENGTH_SHORT).show()
    }

    private fun deleteNote() {
        runBlocking {
            launch(Dispatchers.Default) {
                dao.deleteNote(note)
            }
        }
        onBackPressed()
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(
            this,
            R.style.MyAlertDialogTheme
        ).setMessage("Do you want to delete this note?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteNote()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .setIcon(ContextCompat.getDrawable(this, R.drawable.ic_delete))
            .show()
    }

    private fun entriesAreSameAsLastSave(): Boolean =
        binding.etTitleUpdate.text.toString() == title && binding.etContentUpdate.text.toString() == content

    private fun setupToolbar() {
        binding.toolbarDetails.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mi_delete -> {
                    showDeleteDialog()
                    true
                }
                R.id.mi_setting -> {
                    Toast.makeText(this, "Setting pressed.", Toast.LENGTH_LONG).show()
                    true
                }
                R.id.mi_done -> {
                    if (!entriesAreEmpty() && !entriesAreSameAsLastSave())
                        updateNote()

                    true
                }
                R.id.mi_about -> {
                    Toast.makeText(this, "about pressed.", Toast.LENGTH_LONG).show()
                    true
                }
                else -> false
            }
        }
        binding.toolbarDetails.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}