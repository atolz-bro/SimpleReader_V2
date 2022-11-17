package com.example.simplereader_v1.database.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note WHERE book_name = :bookName")
    suspend fun getNotes(bookName : String) : List<Note>

    @Insert
    suspend fun insertNote(note: Note)

    @Query("SELECT COUNT(*) FROM Note WHERE book_name = :bookName")
    suspend fun getNoteCount(bookName: String) : Int
}