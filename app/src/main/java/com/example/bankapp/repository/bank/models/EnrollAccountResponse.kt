package com.example.bankapp.repository.bank.models

data class EnrollAccountResponse(
    val success: Boolean,
    val data: EnrolledAccount?,
    val errors: List<String>
)