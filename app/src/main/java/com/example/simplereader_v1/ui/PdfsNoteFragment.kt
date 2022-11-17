package com.example.simplereader_v1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.simplereader_v1.Adapters.PdfsNotesAdapter
import com.example.simplereader_v1.MyApplication
import com.example.simplereader_v1.databinding.FragmentPdfsNotesBinding
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModel
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModelFactory

/*
* Fragment to display all Books Notes*/
class PdfsNoteFragment : Fragment() {

    lateinit var binding: FragmentPdfsNotesBinding

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
        binding = FragmentPdfsNotesBinding.inflate(inflater)
        (activity as AppCompatActivity).supportActionBar?.title = "NOTES FROM BOOKS"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PdfsNotesAdapter(requireActivity(),viewModel)
        binding.recyclerView.adapter = adapter
        val it = viewModel.getLibraryBooks()
            if (!it.isEmpty()) {
                binding.noBookInLibrary.visibility = View.GONE
                adapter.submitList(it)
            } else {
                binding.noBookInLibrary.visibility = View.VISIBLE
            }
    }


}