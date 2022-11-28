package com.example.simplereader_v1.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.example.simplereader_v1.database.models.LibraryBook
import com.example.simplereader_v1.database.models.LibraryBookDao
import com.example.simplereader_v1.database.models.Note
import com.example.simplereader_v1.database.models.NoteDao
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LibraryBooksAndNotesViewModel(val bookDao: LibraryBookDao, val noteDao: NoteDao) : ViewModel() {

    val libraryBooks : LiveData<List<LibraryBook>> = bookDao.getLibraryBooksLD()

    fun getLastBatchNo(bookName: String): Int{
        return  getNotes(bookName).map {
            it.batchNo
        }.maxOrNull() ?: 0
    }

    fun getNotes(bookName : String) : List<Note>{
        var it : List<Note>
        runBlocking {
            it = noteDao.getNotes(bookName = bookName)
        }
       return it;
    }

    //Return list of notes/highlights from last batch/Reading session
    fun getRevisionNote(bookName: String): List<Note>{
        var notes = getNotes(bookName).filter {
            //filter out revision_done_note
            !it.text.equals("")
        }
        //notes with largest batch_no
        val lastBatch = getLastBatch(notes)
        notes = notes.filter {
            it.batchNo == lastBatch
        }
        return notes
    }

    fun hasRevised(bookName: String): Boolean{
        val notes = getNotes(bookName)
        if(notes.isEmpty())
            return true
        return notes.last().text.equals("")
    }

    fun getLastBatch(notes: List<Note>): Int{
        return notes.map { it.batchNo }.maxOrNull() ?: 0
    }

    //revision_done_note indicating the last batch note has been revised
    fun addRevisionDoneNote(bookName: String, last_batch: Int){
        addNote(Note(text = "", pageIndex = 0, bookName = bookName, color = 0, batchNo = last_batch))
    }

    fun addNote(note: Note){
        viewModelScope.launch {
            noteDao.insertNote(note);
        }

    }

    fun getLibraryBooks() : List<LibraryBook>{
        var it : List<LibraryBook>
        runBlocking {
            it =  bookDao.getLibraryBooks()
        }
        return it
    }

    fun addLibraryBook(libraryBook: LibraryBook){
        Log.i("RA","inAdd")
        viewModelScope.launch {
            bookDao.insertBook(libraryBook);
        }
    }

    fun updateLibraryBook(libraryBook: LibraryBook){
        viewModelScope.launch {
            bookDao.update(libraryBook)
        }
    }

    fun getBookNotesCount(bookName: String) : Int{
        var count = 0;
        runBlocking {
            count = noteDao.getNoteCount(bookName)
        }
        return count

    }

    fun removeLibraryBook(libraryBook: LibraryBook){
        runBlocking {
            bookDao.delete(libraryBook)
        }
    }

}

class LibraryBooksAndNotesViewModelFactory(val bookDao: LibraryBookDao, val noteDao: NoteDao) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LibraryBooksAndNotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LibraryBooksAndNotesViewModel(bookDao,noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}