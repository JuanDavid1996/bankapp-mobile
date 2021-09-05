package com.example.bankapp.presentation.bank.viewModels

class EnrollAccountForm {
    var isValidNumber = false
    var errorMessageNumber = ""
    var isValidName = false
    var errorMessageName = ""
    var isLoading = false
    fun isValid(): Boolean = isValidName && isValidNumber
}