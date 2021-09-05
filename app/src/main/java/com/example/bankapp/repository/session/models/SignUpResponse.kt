package com.example.bankapp.repository.session.models

data class SignUpResponse(
    val success: Boolean,
    val data: SignUpResponseBody?,
    val errors: List<String>
)