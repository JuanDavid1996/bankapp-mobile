package com.example.bankapp.presentation.bank.viewModels

class TransferToNoEnEnrolledAccountForm {
    var isValidNumber = false
    var errorMessageNumber = ""
    var isValidAmount = false
    var errorMessageAmount = ""
    var isLoading = false


    fun isValid(): Boolean = isValidAmount && isValidNumber
}