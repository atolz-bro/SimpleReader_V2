package com.example.simplereader_v1

import android.app.Application
import com.example.simplereader_v1.database.models.LibraryDatabase

class MyApplication: Application() {
    val database : LibraryDatabase by lazy { LibraryDatabase.getDatabase(this) }
}