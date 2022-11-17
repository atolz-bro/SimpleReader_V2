package com.example.simplereader_v1.Adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplereader_v1.database.models.LibraryBook
import com.example.simplereader_v1.database.models.Note
import com.example.simplereader_v1.databinding.LibraryBookItemBinding
import com.example.simplereader_v1.databinding.NoteItemBinding

class NotesAdapter : ListAdapter<Note, NotesAdapter.NotesViewHolder>(DiffCallBack) {
    class NotesViewHolder(var binding : NoteItemBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            NoteItemBinding.inflate(LayoutInflater.from(parent.context))

        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            text.text = item.text
            text.setOnClickListener {
                (it as TextView).maxLines=100
            }
            text.setBackgroundColor(item.color)
            Log.d("Text",item.text)
        }
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}