package com.app.notes.data.repository

import androidx.lifecycle.LiveData
import com.app.notes.data.room.dao.UserDao
import com.app.notes.data.room.entity.User


/*
*
* Repository class to perform users query in local database
*
* */


class UserRepository(private val userDao: UserDao) {

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }


    fun getUser(userId: String): LiveData<User> {
        return userDao.getUser(userId)
    }

}