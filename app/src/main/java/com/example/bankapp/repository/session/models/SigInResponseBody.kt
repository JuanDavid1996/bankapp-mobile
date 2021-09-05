package com.example.bankapp.repository.session.models

data class SigInResponseBody(
    var user: User,
    var token: String?
)