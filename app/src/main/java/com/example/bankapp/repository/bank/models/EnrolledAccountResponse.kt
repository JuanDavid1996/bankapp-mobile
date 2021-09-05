package com.example.bankapp.repository.bank.models

data class EnrolledAccountResponse(
    val success: Boolean,
    val data: List<EnrolledAccount>?,
    val errors: List<String>
)