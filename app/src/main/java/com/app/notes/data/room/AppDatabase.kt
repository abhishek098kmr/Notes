package com.app.notes.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.notes.MyApplication
import com.app.notes.data.room.dao.UserNoteDao
import com.app.notes.data.room.entity.Note
import com.app.notes.data.room.entity.User
import com.app.notes.utils.Constants

@Database(entities = [User::class, Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserNoteDao(): UserNoteDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    MyApplication.getInstance()!!,
                    AppDatabase::class.java, Constants.APP_DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}