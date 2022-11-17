package com.example.simplereader_v1.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "text")
    val text : String,
    @ColumnInfo(name = "page_index")
    val pageIndex : Int,
    @ColumnInfo(name = "book_name")
    val bookName : String,
    @ColumnInfo(name = "color")
    val color : Int,
    @ColumnInfo(name = "batch")
    val batchNo : Int

)