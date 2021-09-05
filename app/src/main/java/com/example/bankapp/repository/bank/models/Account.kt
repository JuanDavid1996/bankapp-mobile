package com.example.bankapp.repository.bank.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Account(
    @PrimaryKey val _id: String,
    @ColumnInfo val number: String,
    @ColumnInfo val type: String,
    @ColumnInfo val owner: String,
    @ColumnInfo val balance: Double
) {
    override fun toString(): String {
        return this.number;
    }
}
