package com.example.bankapp.repository.session.localstorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bankapp.repository.session.models.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    fun getCurrentUser(): User?

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)
}