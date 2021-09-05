package com.example.bankapp.repository.session.localstorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bankapp.repository.session.models.Session

@Dao
interface SessionDao {
    @Query("SELECT * FROM session LIMIT 1")
    fun getCurrentSession(): Session?

    @Insert
    fun insert(session: Session)

    @Delete
    fun delete(session: Session)
}