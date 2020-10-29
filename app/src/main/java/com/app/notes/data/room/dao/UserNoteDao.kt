package com.app.notes.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.notes.data.room.entity.Note
import com.app.notes.data.room.entity.User

@Dao
interface UserNoteDao {

    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUser(userId: String): LiveData<User>

    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY modified DESC")
    fun getUserNotes(userId: String): LiveData<List<Note>>

    @Insert
    suspend fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}