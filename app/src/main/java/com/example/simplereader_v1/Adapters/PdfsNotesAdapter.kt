package com.example.simplereader_v1.Adapters

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplereader_v1.MainActivity
import com.example.simplereader_v1.MyPdfActivity
import com.example.simplereader_v1.R
import com.example.simplereader_v1.database.models.LibraryBook
import com.example.simplereader_v1.database.models.Note
import com.example.simplereader_v1.databinding.NoteItemBinding
import com.example.simplereader_v1.databinding.PdfNotesItemBinding
import com.example.simplereader_v1.ui.NotesFragment
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModel

/*Converts LibraryBook to PdfNotesItemBinding
--Thumbnail and Notes Count*/
class PdfsNotesAdapter(val activity: FragmentActivity,val viewModel : LibraryBooksAndNotesViewModel) : ListAdapter<LibraryBook, PdfsNotesAdapter.PdfsNoteViewHolder>(DiffCallBack) {
    class PdfsNoteViewHolder(var binding : PdfNotesItemBinding) : RecyclerView.ViewHolder(binding.root){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfsNoteViewHolder {
        return PdfsNoteViewHolder(PdfNotesItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: PdfsNoteViewHolder, position: Int) {
        val item = getItem(position)
        val uri = Uri.parse(item.thumbnailUri)

        /*Sets thumbnail and note count from LibraryBook*/
        holder.binding.noteThumbnail.setImageURI(uri)
        holder.binding.noteCount.text = activity.resources.getString(R.string.notes_count,viewModel.getBookNotesCount(item.name))


        val notesFragment = NotesFragment()
        val bundle = Bundle()
        bundle.putString(MainActivity.LIBRARY_BOOK_NAME,item.name)
        notesFragment.arguments = bundle
        holder.binding.root.setOnClickListener(){
            activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,notesFragment)
                .addToBackStack("note_frag")
                .commit()
        }


    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<LibraryBook>() {
            override fun areItemsTheSame(oldItem: LibraryBook, newItem: LibraryBook): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: LibraryBook, newItem: LibraryBook): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}