package com.example.bankapp.repository.bank.models

data class AccountResponse(
    val success: Boolean,
    val data: List<Account>?,
    val errors: List<String>
)