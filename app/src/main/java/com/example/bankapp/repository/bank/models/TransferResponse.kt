package com.example.bankapp.repository.bank.models

data class TransferResponse(
    val success: Boolean,
    val data: Transfer?,
    val errors: List<String>
)