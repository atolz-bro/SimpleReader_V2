package com.example.simplereader_v1

import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import com.example.simplereader_v1.databinding.ActivityMainBinding
import com.example.simplereader_v1.ui.LibraryBooksFragment
import com.example.simplereader_v1.ui.PdfsNoteFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    companion object{
        val LIBRARY_BOOK_NAME = "bookName"
        val LOCATION_URI = "locationUri"
        val THUMNAIL_URI = "thumbnailUri"
        val ID = "id"
        val PAGE_COUNT = "pageCount"
        val CURRENT_PAGE = "currentPage"
        val NOTE_COUNT = "notesCount"
        val TIME_SPENT = "timeSpent"

    }

    lateinit var fragMngr : FragmentManager
    lateinit var bottomNav : BottomNavigationView
    lateinit var activityMainBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        fragMngr = supportFragmentManager

        Log.d("LC","in MA on create")
        Log.d("Tag","OnCreate")
        fragMngr.beginTransaction()
            .replace(
                R.id.fragment_container,
                fragMngr.findFragmentById(R.id.fragment_container)
                    ?: LibraryBooksFragment()
            )
            .commit()


        bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.setOnItemSelectedListener {
            Log.d("Tag","Clicked")
            when (it.itemId) {
                R.id.note -> {

                    fragMngr.beginTransaction()
                        .replace(R.id.fragment_container,PdfsNoteFragment())
                        .addToBackStack("library_frag")
                        .commit()
                    Log.d("Tag","Clicked")
                    true
                }
                R.id.library -> {
                    fragMngr.beginTransaction()
                        .replace(R.id.fragment_container,LibraryBooksFragment())
                        .commit()
                    true
                }
                else -> { false}
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                fragMngr.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        Log.d("LC","in MA on pause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("LC","in MA on stop")
        super.onStop()
    }
}