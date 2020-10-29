package com.app.notes.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.notes.data.room.entity.Note
import com.app.notes.data.room.entity.User
import com.app.notes.data.repository.MainRepository
import com.app.notes.data.room.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var repository: MainRepository
    private var userNotes: LiveData<List<Note>>

    init {
        val userNoteDao = AppDatabase.getDatabase().UserNoteDao()
        repository = MainRepository(userNoteDao)
        userNotes = repository.userNotes
    }

    fun getUserNotes(): LiveData<List<Note>> {
        return userNotes
    }


    fun addUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.addUser(user)
    }


    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.addNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }


    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(note)
    }

    fun getUser(userId: String): LiveData<User> {
        return repository.getUser(userId)
    }


}