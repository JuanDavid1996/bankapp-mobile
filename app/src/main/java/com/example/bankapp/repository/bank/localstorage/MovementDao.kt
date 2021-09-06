package com.example.bankapp.repository.bank.localstorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bankapp.repository.bank.models.Movement

@Dao
interface MovementDao {
    @Insert
    fun insert(movement: Movement)

    @Query("SELECT * FROM Movement WHERE account = :accountId")
    fun getMovementsByAccountId(accountId: String): List<Movement>

    @Delete
    fun delete(movement: Movement)
}