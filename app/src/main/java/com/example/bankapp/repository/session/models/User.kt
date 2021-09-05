package com.example.bankapp.repository.session.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var _id: String,
    @ColumnInfo var identification: String,
    @ColumnInfo var username: String,
    @ColumnInfo(name = "first_name") var firstName: String,
    @ColumnInfo(name = "last_name") var lastName: String
)