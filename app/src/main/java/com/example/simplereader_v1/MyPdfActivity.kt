package com.example.simplereader_v1

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.viewModels
import com.example.simplereader_v1.database.models.LibraryBook
import com.example.simplereader_v1.database.models.Note
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModel
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModelFactory
import com.pspdfkit.ui.PdfActivity
import com.pspdfkit.ui.toolbar.popup.PopupToolbarMenuItem

class MyPdfActivity : PdfActivity() {
    val viewModel : LibraryBooksAndNotesViewModel by viewModels{
        LibraryBooksAndNotesViewModelFactory(
            (application as MyApplication).database.libraryBookDao(),
            (application as MyApplication).database.noteDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuItems = listOf(
            PopupToolbarMenuItem(R.id.highlight,R.string.highlight),
            PopupToolbarMenuItem(R.id.add_to_note,R.string.add_to_note),
            PopupToolbarMenuItem(R.id.dictionary,R.string.dictionary)
        )
        val bundle = intent.extras
        val name =  bundle?.getString(MainActivity.LIBRARY_BOOK_NAME);

        pdfFragment?.setOnPreparePopupToolbarListener(){x->
            x.menuItems = menuItems
            x.setOnPopupToolbarItemClickedListener {
                when(it.id){
                    R.id.highlight ->{
                        x.controller?.highlightSelectedText()
                        true
                    }
                    /*R.id.add_to_note ->{
                        val text = pdfFragment?.textSelection?.text
                        val pageIndex = pageIndex
                        val bookName = name

                        val note = Note(
                            text = text!!,
                            pageIndex = pageIndex,
                            bookName = bookName!!
                        )
                        viewModel.addNote(note)


                        pdfFragment?.exitCurrentlyActiveMode()
                        true
                    }*/
                    else -> false
                }
            }
        }
    }
}