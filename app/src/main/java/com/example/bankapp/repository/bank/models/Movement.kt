package com.example.bankapp.repository.bank.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movement(
    @PrimaryKey val _id: String,
    @ColumnInfo val amount: String,
    @ColumnInfo val description: String,
    @ColumnInfo val currency: String,
    @ColumnInfo val movement: String,
    @ColumnInfo val account: String
)
