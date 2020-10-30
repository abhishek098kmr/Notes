package com.app.notes.data.room.dao

import androidx.paging.DataSource
import androidx.room.*
import com.app.notes.data.room.entity.Note

@Dao
interface NoteDao {


    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY modified DESC")
    fun getUserNotes(userId: String): DataSource.Factory<Int,Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes WHERE userId=:userId AND (title LIKE :userInput OR description LIKE :userInput)")
    fun searchNotes(userId: String, userInput: String): DataSource.Factory<Int,Note>
}