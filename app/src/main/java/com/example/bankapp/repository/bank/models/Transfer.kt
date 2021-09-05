package com.example.bankapp.repository.bank.models

data class Transfer(
    val _id: String,
    val amount: Int,
    val currency: String,
    val trm: String,
    val origin: String
)
