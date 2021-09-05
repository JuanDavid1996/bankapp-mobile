package com.example.bankapp.repository.session.models

data class SignInResponse(
    var success: Boolean,
    var data: SigInResponseBody?,
    var errors: List<String>
)