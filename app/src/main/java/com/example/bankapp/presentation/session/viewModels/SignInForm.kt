package com.example.bankapp.presentation.session.viewModels

class SignInForm {
    var isValidUsername = false
    var isValidPassword = false

    var usernameMessageValidation = ""
    var passwordMessageValidation = ""

    var isLoading = false

    fun isSuccess(): Boolean = isValidPassword && isValidPassword
}