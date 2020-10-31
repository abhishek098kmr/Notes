package com.app.notes.data.room.dao

import androidx.paging.DataSource
import androidx.room.*
import com.app.notes.data.room.entity.Note

@Dao
interface NoteDao {


    @Query("SELECT * FROM notes WHERE userId = :userId ORDER BY (CASE WHEN isPinned=1 THEN 0  ELSE 1 END), modifiedPinned DESC")
    fun getUserNotes(userId: String): DataSource.Factory<Int, Note>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Query("UPDATE notes SET title= :title , description= :description , modified= :modified , modifiedPinned= :modified WHERE id= :id")
    suspend fun updateNote(id: Int, title: String, description: String, modified: Long)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM notes WHERE userId=:userId AND (title LIKE :userInput OR description LIKE :userInput) ORDER BY (CASE WHEN isPinned=1 THEN 0  ELSE 1 END), modifiedPinned DESC")
    fun searchNotes(userId: String, userInput: String): DataSource.Factory<Int, Note>

    @Query("UPDATE notes SET isPinned= :isPinned, modifiedPinned= :modified WHERE id= :id")
    suspend fun updatedPinnedStatus(id: Int, isPinned: Boolean, modified: Long)
}