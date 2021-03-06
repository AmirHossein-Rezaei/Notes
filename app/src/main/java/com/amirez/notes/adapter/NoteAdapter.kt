package com.amirez.notes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amirez.notes.NoteEvent
import com.amirez.notes.databinding.ItemNoteBinding
import com.amirez.notes.model.Note

class NoteAdapter(private var notes: ArrayList<Note>, private val noteEvent: NoteEvent) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private lateinit var binding: ItemNoteBinding

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindViews(note: Note, pos: Int) {
            binding.tvTitleItem.text = note.title
            binding.tvContent.text = note.content


            itemView.setOnClickListener { noteEvent.onNoteClicked(note) }
            itemView.setOnLongClickListener {
                noteEvent.onNoteLongClicked(note, pos)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bindViews(note, position)
    }

    override fun getItemCount(): Int = notes.size

    fun itemDeleted(note: Note, pos: Int) {
        notes.remove(note)
        notifyItemRemoved(pos)
    }


}