package com.example.simplereader_v1.database.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LibraryBook::class,Note::class], version = 6, exportSchema = false)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun libraryBookDao() : LibraryBookDao
    abstract  fun noteDao() : NoteDao

    companion object{
        @Volatile
        private var INSTANCE: LibraryDatabase? = null;

        fun getDatabase(context : Context) : LibraryDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LibraryDatabase::class.java,
                    "library_database")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}