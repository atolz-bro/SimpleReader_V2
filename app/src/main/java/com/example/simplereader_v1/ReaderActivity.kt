package com.example.simplereader_v1

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import com.example.simplereader_v1.MainActivity
import com.example.simplereader_v1.R
import com.example.simplereader_v1.database.models.Note
import com.example.simplereader_v1.ui.RevisionFragment
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModel
import com.example.simplereader_v1.viewModels.LibraryBooksAndNotesViewModelFactory
import com.pspdfkit.annotations.HighlightAnnotation
import com.pspdfkit.configuration.PdfConfiguration
import com.pspdfkit.configuration.page.PageScrollDirection
import com.pspdfkit.configuration.page.PageScrollMode
import com.pspdfkit.datastructures.TextSelection
import com.pspdfkit.internal.va
import com.pspdfkit.ui.PdfFragment
import com.pspdfkit.ui.special_mode.controller.TextSelectionController
import com.pspdfkit.ui.special_mode.manager.TextSelectionManager
import java.util.*

class ReaderActivity : AppCompatActivity(),TextSelectionManager.OnTextSelectionChangeListener, TextSelectionManager.OnTextSelectionModeChangeListener {
    val viewModel : LibraryBooksAndNotesViewModel by viewModels{
        LibraryBooksAndNotesViewModelFactory(
            (this.application as MyApplication).database.libraryBookDao(),
            (this.application as MyApplication).database.noteDao()
        )
    }
    var bookName = ""
    var isHighlighting = false
    lateinit var fragment : Fragment
    var last_batch  = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reader_activity)

        val bundle = intent.extras
        val documentUri =  Uri.parse(bundle?.getString(MainActivity.LOCATION_URI));
        bookName = bundle?.getString(MainActivity.LIBRARY_BOOK_NAME)!!
        last_batch = viewModel.getLastBatchNo(bookName)
        val configuration = PdfConfiguration.Builder()
            .scrollDirection(PageScrollDirection.VERTICAL)
            .scrollMode(PageScrollMode.CONTINUOUS)
            .build()
        // First, try to restore a previously created fragment. If no fragment exists, create a new one.
        fragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) ?: createFragment(documentUri, configuration)

        val highlight = findViewById<TextView>(R.id.highlight)
        highlight.setOnClickListener(){
            findViewById<LinearLayout>(R.id.options_panel).visibility = View.GONE
            findViewById<LinearLayout>(R.id.color_panel).visibility = View.VISIBLE
            isHighlighting =true
        }
        val addNote = findViewById<TextView>(R.id.addNote)
        addNote.setOnClickListener {
            findViewById<LinearLayout>(R.id.options_panel).visibility = View.GONE
            findViewById<LinearLayout>(R.id.color_panel).visibility = View.VISIBLE
            //(fragment as PdfFragment).exitCurrentlyActiveMode()
            isHighlighting = false
            Log.i(TAG, "in Add Note")
            //Log.i(TAG, viewModel.getNotes(bookName).toString())

        }
        val green = findViewById<View>(R.id.green)
        val yellow = findViewById<View>(R.id.yellow)
        val blue = findViewById<View>(R.id.blue)

        green.setOnClickListener(){
            val pdfFragment = (fragment as PdfFragment)
            pdfFragment.apply {
                val colorHighlight = HighlightAnnotation(pageIndex,textSelection?.textBlocks!!)
                colorHighlight.apply {
                    color = Color.GREEN
                    alpha = 0.2f
                }
                if(isHighlighting){
                    addAnnotationToPage(colorHighlight,true)
                }else{
                    addAnnotationToPage(colorHighlight,true)
                    viewModel.addNote(
                        Note(text = textSelection?.text!!,
                            pageIndex = pageIndex,
                            bookName = bookName,
                            color = Color.GREEN,
                            batchNo = last_batch+1))
                }
                exitCurrentlyActiveMode()
                findViewById<LinearLayout>(R.id.color_panel).visibility = View.GONE
            }
        }

        yellow.setOnClickListener(){
            val pdfFragment = (fragment as PdfFragment)
            pdfFragment.apply {
                val colorHighlight = HighlightAnnotation(pageIndex,textSelection?.textBlocks!!)
                colorHighlight.apply {
                    color = Color.YELLOW
                    alpha = 0.2f
                }
                if(isHighlighting){
                    addAnnotationToPage(colorHighlight,true)
                }else{
                    addAnnotationToPage(colorHighlight,true)
                    viewModel.addNote(
                        Note(text = textSelection?.text!!,
                            pageIndex = pageIndex,
                            bookName = bookName,
                            color = Color.YELLOW,
                            batchNo = last_batch+1))
                }
                exitCurrentlyActiveMode()
                findViewById<LinearLayout>(R.id.color_panel).visibility = View.GONE
            }
        }

        setOnClickListener(R.id.blue,Color.argb(100,0,0,200));



    }

    override fun onResume() {
        super.onResume()
        if(!viewModel.hasRevised(bookName)){
            RevisionFragment().show(supportFragmentManager,"revision_dialog")
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.more_options,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        RevisionFragment().show(supportFragmentManager,"revision_dialog")
        return super.onOptionsItemSelected(item)
    }


    private fun createFragment(documentUri: Uri, configuration: PdfConfiguration): PdfFragment {
        val fragment = PdfFragment.newInstance(documentUri, configuration)
        fragment.addOnTextSelectionModeChangeListener(this)
        fragment.addOnTextSelectionChangeListener(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        return fragment
    }

    private fun setOnClickListener(id: Int, btnColor : Int){
        val colorButton = findViewById<View>(id)
        colorButton.setOnClickListener(){
            val pdfFragment = (fragment as PdfFragment)
            pdfFragment.apply {
                val colorHighlight = HighlightAnnotation(pageIndex,textSelection?.textBlocks!!)
                colorHighlight.apply {
                    color = btnColor
                    alpha = 0.2f
                }
                if(isHighlighting){
                    addAnnotationToPage(colorHighlight,true)
                }else{
                    addAnnotationToPage(colorHighlight,true)
                    viewModel.addNote(
                        Note(text = textSelection?.text!!,
                            pageIndex = pageIndex,
                            bookName = bookName,
                            color = btnColor,
                            batchNo = last_batch+1))
                }
                exitCurrentlyActiveMode()
                findViewById<LinearLayout>(R.id.color_panel).visibility = View.GONE
            }
        }
    }

    val TAG = "MyActivityTextSelection"



    override fun onEnterTextSelectionMode(controller: TextSelectionController) {
        Log.i(TAG, "Text selection mode has started.")
        findViewById<LinearLayout>(R.id.options_panel).visibility = View.VISIBLE
    }

    override fun onExitTextSelectionMode(controller: TextSelectionController) {
        Log.i(TAG, "Text selection mode has ended.")
        findViewById<LinearLayout>(R.id.options_panel).visibility = View.GONE
        findViewById<LinearLayout>(R.id.color_panel).visibility = View.GONE


    }


    override fun onBeforeTextSelectionChange(p0: TextSelection?, p1: TextSelection?): Boolean {
       return true
    }

    override fun onAfterTextSelectionChange(p0: TextSelection?, p1: TextSelection?) {

    }
}