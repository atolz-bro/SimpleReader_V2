package com.example.simplereader_v1.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ListAdapter
import com.example.simplereader_v1.Adapters.LibraryBooksFragmentAdapter
import com.example.simplereader_v1.MyApplication
import com.example.simplereader_v1.R
import com.example.simplereader_v1.database.models.LibraryBook
import com.example.simplereader_v1.databinding.FragmentLibraryBooksBinding
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModel
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pspdfkit.document.PdfDocumentLoader
import java.io.File
import java.io.IOException

class LibraryBooksFragment : Fragment() {
    lateinit var bottomNav : BottomNavigationView

    lateinit var binding : FragmentLibraryBooksBinding

    lateinit var adapter : ListAdapter<LibraryBook,LibraryBooksFragmentAdapter.LibraryBookViewHolder>

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
        binding = FragmentLibraryBooksBinding.inflate(inflater,container,false)
        (activity as AppCompatActivity).supportActionBar?.title = "LIBRARY "

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LibraryBooksFragmentAdapter(requireActivity()) { viewModel.removeLibraryBook(it) }
        binding.recyclerView.adapter = adapter


        binding.addFromPhone.setOnClickListener(){
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.setType("application/pdf")
            startActivityForResult(intent,1)
        }
        binding.addBookFab.setOnClickListener(){
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.setType("application/pdf")
            startActivityForResult(intent,1)
        }

        viewModel.libraryBooks.observe(viewLifecycleOwner){
            if (!it.isEmpty()) {
                binding.noBookInLibrary.visibility = View.GONE
                binding.addFromPhone.visibility = View.GONE
                adapter.submitList(it)
            } else {
                binding.noBookInLibrary.visibility = View.VISIBLE
                binding.addFromPhone.visibility = View.VISIBLE

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            val uri = data?.data
            val contentResolver = requireActivity().contentResolver

            val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
// Check for the freshest data.
            if (uri != null) {
                contentResolver.takePersistableUriPermission(uri, takeFlags)
            }
            val locationUri = data?.data.toString()
            val thumbnailUri = decodeThumbnail(uri)
            val name = PdfDocumentLoader.openDocument(requireContext(),uri!!).title
            val libraryBook = LibraryBook(
                name = name!!,
                locationUri = locationUri,
                thumbnailUri = thumbnailUri,
            )
            viewModel.addLibraryBook(libraryBook)

        }
    }

    override fun onResume() {
        super.onResume()
        /*val it = viewModel.getLibraryBooks()
        if (!it.isEmpty()) {
            binding.noBookInLibrary.visibility = View.GONE
            binding.addFromPhone.visibility = View.GONE
            adapter.submitList(it)
        } else {
            binding.noBookInLibrary.visibility = View.VISIBLE
            binding.addFromPhone.visibility = View.VISIBLE

        }*/
        bottomNav.menu.findItem(R.id.library).setChecked(true)
    }

    /*
    * Decode the first page of the pdf as the thumbnail
    * Saves The bitmap image to internal storage and return the uri location
    * Return the string of the uri location
    * */
    private fun decodeThumbnail(uri : Uri?) : String{
        var image_thumbnail = ""
        try{
            val document = PdfDocumentLoader.openDocument(requireContext(), uri!!);
            //file name in internal storage dir
            image_thumbnail = document.title + "thumbnail"
            val pageSize = document.getPageSize(0)
            //Decode Thumbnail
            val bitmap = document.renderPageToBitmap(requireContext(),0, (pageSize.width/2).toInt(),
                (pageSize.height/2).toInt()
            )

            //Write Thumbnail to storage
            requireActivity().openFileOutput(image_thumbnail,Context.MODE_PRIVATE).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)
            }

        }catch (e : IOException){
            e.printStackTrace()
        }
        val file: File = File(requireActivity().getFilesDir(), image_thumbnail)
        Log.d("Tag","${file.toURI()}")
        return  file.toURI().toString()
    }
}