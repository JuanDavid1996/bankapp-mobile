package com.example.bankapp.repository.bank.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EnrolledAccount(
    @PrimaryKey val _id: String,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "account_type") val accountType: String,
    @ColumnInfo(name = "account_number") val accountNumber: String,
)