package com.example.simplereader_v1.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_book")
data class LibraryBook(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String,
    val locationUri : String,
    val thumbnailUri : String,
)