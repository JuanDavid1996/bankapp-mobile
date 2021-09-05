package com.example.bankapp.repository.session.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey val token: String,
    @ColumnInfo val userId: String
)