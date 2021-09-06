package com.example.bankapp.repository.bank.localstorage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.bankapp.repository.bank.models.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM Account")
    fun getAccounts(): List<Account>

    @Insert
    fun insert(vararg account: Account)

    @Delete
    fun delete(account: Account)

    @Query("SELECT * FROM Account WHERE number = :number LIMIT 1")
    fun getAccountByAccountNumber(number: String): Account

    @Query("SELECT * FROM Account WHERE _id = :id LIMIT 1")
    fun getAccountById(id: String): Account
}