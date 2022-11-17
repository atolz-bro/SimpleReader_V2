package com.example.simplereader_v1.ui

import android.app.Dialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.simplereader_v1.Adapters.RevisonFragmentViewPager2Adapter
import com.example.simplereader_v1.MyApplication
import com.example.simplereader_v1.R
import com.example.simplereader_v1.ReaderActivity
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModel
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModelFactory
import com.pspdfkit.internal.va

class RevisionFragment : DialogFragment() {
    val viewModel : LibraryBooksAndNotesViewModel by activityViewModels{
        LibraryBooksAndNotesViewModelFactory(
            (requireActivity().application as MyApplication).database.libraryBookDao(),
            (requireActivity().application as MyApplication).database.noteDao()
        )
    }
    lateinit var readerActivity: ReaderActivity

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val revisionNotes = viewModel.getRevisionNote(readerActivity.bookName)
        val builder = AlertDialog.Builder(requireActivity())
        val view = layoutInflater.inflate(R.layout.fragment_revision,null)
        val myViewPager : ViewPager2 = view.findViewById<ViewPager2>(R.id.my_view_pager)
        val closeRevision: ImageView = view.findViewById(R.id.close_revision)
        val prevNote : ImageView =  view.findViewById(R.id.prev_note_icon)
        val nextNote : ImageView =  view.findViewById(R.id.next_note_icon)
        prevNote.setOnClickListener(){

        }

        myViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                view.findViewById<TextView>(R.id.note_count).text = getString(R.string.revision_note_count,position+1,revisionNotes.size)
            }
        })
        myViewPager.adapter = RevisonFragmentViewPager2Adapter(this,revisionNotes)
        val revisionDialog = builder.setView(view).create()
        closeRevision.setOnClickListener(){
            this@RevisionFragment.dismiss()
        }
        return revisionDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        readerActivity = (requireActivity() as ReaderActivity)
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        viewModel.addRevisionDoneNote(readerActivity.bookName,readerActivity.last_batch)
        super.onDestroy()
    }
}