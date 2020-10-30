package com.app.notes.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.app.notes.data.repository.NotesRepository
import com.app.notes.data.repository.UserRepository
import com.app.notes.data.room.AppDatabase
import com.app.notes.data.room.entity.Note
import com.app.notes.data.room.entity.User
import com.app.notes.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private var noteRepository: NotesRepository
    private var userRepository: UserRepository
    lateinit var userNotes: LiveData<PagedList<Note>>
     val getUserNotes = MutableLiveData<String>()

    init {
        val database = AppDatabase.getDatabase(application)
        userRepository = UserRepository(database.userDao())
        noteRepository = NotesRepository(database.noteDao())

    }


    fun initializeNotes() {
        val config = PagedList.Config.Builder()
            .setPageSize(Constants.PAGE_LIMIT)
            .setEnablePlaceholders(false)
            .build()
        userNotes = Transformations.switchMap(getUserNotes) { input ->
            if (input.isEmpty()) {
                noteRepository.getUserNotes(config)
            } else {
                noteRepository.searchNotes(input, config)
            }
        }
    }
/*

    fun getUserNotes(searchKeyWord: String): LiveData<List<Note>> {
        return noteRepository.getUserNotes(searchKeyWord)
    }
*/


    fun addUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.addUser(user)
    }

    fun getUser(userId: String): LiveData<User> {
        return userRepository.getUser(userId)
    }


    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.addNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.updateNote(note)
    }


    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.deleteNote(note)
    }

    /* fun searchNotes(searchKeyWord: String): LiveData<List<Note>> {
         return noteRepository.searchNotes(searchKeyWord)
     }*/

}