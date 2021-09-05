package com.example.bankapp.presentation.session.viewModels

class SignUpForm {
    var isValidFirstName = false
    var isValidLastName = false
    var isValidIdentification = false
    var isValidUsername = false
    var isValidPassword = false
    var isValidPasswordConfirmation = false

    var firstNameMessageValidation = ""
    var lastNameMessageValidation = ""
    var identificationMessageValidation = ""
    var usernameMessageValidation = ""
    var passwordMessageValidation = ""
    var passwordConfirmationMessageValidation = ""

    var isLoading = false

    fun isSuccess(): Boolean {
        return (
                isValidFirstName && isValidLastName && isValidIdentification &&
                        isValidUsername && isValidPassword && isValidPasswordConfirmation)
    }
}