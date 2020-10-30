package com.app.notes.data.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.notes.data.room.dao.NoteDao
import com.app.notes.data.room.dao.UserDao
import com.app.notes.data.room.entity.Note
import com.app.notes.data.room.entity.User
import com.app.notes.utils.Constants

@Database(entities = [User::class, Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao


    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(application: Application): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application.applicationContext!!,
                    AppDatabase::class.java, Constants.APP_DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}