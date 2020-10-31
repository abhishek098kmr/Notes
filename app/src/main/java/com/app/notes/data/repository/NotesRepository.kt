package com.app.notes.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.notes.data.room.dao.NoteDao
import com.app.notes.data.room.entity.Note
import com.app.notes.utils.Constants
import com.app.notes.utils.PreferenceUtil


/*
*
* Repository class perform  notes query to local database
*
* */


class NotesRepository(private val noteDao: NoteDao) {

    fun getUserNotes(config: PagedList.Config): LiveData<PagedList<Note>> {
        return LivePagedListBuilder(
            noteDao.getUserNotes(
                PreferenceUtil.getString(
                    Constants.PREF_USER_ID,
                    ""
                )
            ), config
        ).build()
    }

    suspend fun addNote(note: Note) {
        noteDao.addNote(note)
    }

    suspend fun updateNote(id: Int, title: String, description:String, modified:Long) {
        noteDao.updateNote(id,title,description,modified)
    }

   suspend fun updatedPinnedStatus(id: Int, isPinned: Boolean,modified: Long) {
        noteDao.updatedPinnedStatus(id, isPinned,modified)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    fun searchNotes(userInput: String, config: PagedList.Config): LiveData<PagedList<Note>> {
        return LivePagedListBuilder(
            noteDao.searchNotes(
                PreferenceUtil.getString(Constants.PREF_USER_ID, ""),
                userInput
            ), config
        ).build()
    }

}