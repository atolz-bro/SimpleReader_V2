package com.example.simplereader_v1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simplereader_v1.Adapters.NotesAdapter
import com.example.simplereader_v1.MainActivity
import com.example.simplereader_v1.MyApplication
import com.example.simplereader_v1.R
import com.example.simplereader_v1.databinding.FragmentNotesBinding
import com.example.simplereader_v1.databinding.FragmentPdfsNotesBinding
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModel
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pspdfkit.ui.PdfFragment

/**
 * Fragment to display Notes belonging to a particular Book*/
class NotesFragment : Fragment() {
    lateinit var binding: FragmentNotesBinding
    lateinit var bottomNav : BottomNavigationView

    val viewModel : LibraryBooksAndNotesViewModel by activityViewModels{
        LibraryBooksAndNotesViewModelFactory(
            (activity?.application as MyApplication).database.libraryBookDao(),
            (activity?.application as MyApplication).database.noteDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)!!
        bottomNav.visibility = View.GONE
        binding = FragmentNotesBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "NOTES"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = NotesAdapter()
        binding.recyclerView.adapter = adapter
        val bookName = arguments?.getString(MainActivity.LIBRARY_BOOK_NAME)
        val it = viewModel.getNotes(bookName!!)
            adapter.submitList(it.filter {
                //don't display revision_done note
                !it.text.equals("") })
    }

    override fun onDestroy() {
        super.onDestroy()
         bottomNav.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }
}