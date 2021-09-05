package com.example.bankapp.repository.bank.localstorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bankapp.repository.bank.models.EnrolledAccount

@Dao
interface EnrolledAccountDao {
    @Query("SELECT * FROM EnrolledAccount")
    fun getEnrolledAccount(): List<EnrolledAccount>

    @Insert
    fun insert(vararg enrolledAccount: EnrolledAccount)

    @Delete
    fun delete(vararg enrolledAccounts: EnrolledAccount)

    @Query("SELECT * FROM EnrolledAccount WHERE _id = :id LIMIT 1")
    fun findById(id: String): EnrolledAccount
}