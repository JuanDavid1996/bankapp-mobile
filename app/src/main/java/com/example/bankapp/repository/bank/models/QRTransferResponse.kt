package com.example.bankapp.repository.bank.models

data class QRTransferResponse(
    val success: Boolean,
    val data: String?,
    val errors: List<String>
)