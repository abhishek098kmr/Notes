package com.app.notes.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.notes.data.room.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUser(userId: String): LiveData<User>

    @Insert
    suspend fun addUser(user: User)
}