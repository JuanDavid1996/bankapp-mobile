package com.example.bankapp.repository.bank.models

data class MovementResponse(
    val success: Boolean,
    val data: List<Movement>?,
    val errors: List<String>
)
