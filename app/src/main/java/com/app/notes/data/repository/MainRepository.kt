package com.app.notes.data.repository

import androidx.lifecycle.LiveData
import com.app.notes.data.room.entity.Note
import com.app.notes.data.room.entity.User
import com.app.notes.data.room.dao.UserNoteDao
import com.app.notes.utils.Constants
import com.app.notes.utils.PreferenceUtil


/*
*
* Repository class to get
*
* */

class MainRepository(private val userNoteDao: UserNoteDao) {
    val userNotes: LiveData<List<Note>> =
        userNoteDao.getUserNotes(PreferenceUtil.getString(Constants.PREF_USER_ID, ""))

    suspend fun addUser(user: User) {
        userNoteDao.addUser(user)
    }


    suspend fun addNote(note: Note) {
        userNoteDao.addNote(note)
    }

    suspend fun updateNote(note: Note) {
        userNoteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        userNoteDao.deleteNote(note)
    }

    fun getUser(userId: String): LiveData<User> {
        return userNoteDao.getUser(userId)
    }

}