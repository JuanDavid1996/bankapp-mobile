package com.example.bankapp.presentation.home.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.contants.ModelConstants
import com.example.bankapp.repository.bank.AccountRepository
import com.example.bankapp.repository.bank.TransferRepository
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.repository.common.models.Result
import kotlinx.coroutines.launch

class TransferToNotEnrolledAccountViewModel : ViewModel() {

    private val accountRepository = AccountRepository()
    private val transferRepository = TransferRepository()

    val accounts: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>(emptyList())
    }

    val model: MutableLiveData<TransferToNoEnEnrolledAccountForm> by lazy {
        MutableLiveData<TransferToNoEnEnrolledAccountForm>(TransferToNoEnEnrolledAccountForm())
    }

    val onSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun onCreate() {
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts(false)) {
                is Result.Success -> {
                    accounts.postValue(result.data.data)
                }
            }
        }
    }

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    private fun validateForm(
        number: String,
        amount: String
    ): TransferToNoEnEnrolledAccountForm {
        val newFormState = TransferToNoEnEnrolledAccountForm()
        if (number.length < 11) {
            newFormState.isValidNumber = false
            newFormState.errorMessageNumber = "Número de cuenta no válido"
        } else {
            newFormState.isValidNumber = true
            newFormState.errorMessageNumber = ""
        }

        if (amount.isEmpty() || amount.toInt() < 1) {
            newFormState.isValidAmount = false
            newFormState.errorMessageAmount = "Por favor ingrese un monto valido"
        } else {
            newFormState.isValidAmount = true
            newFormState.errorMessageAmount = ""
        }

        return newFormState
    }

    private fun getAccountTypeName(accountType: String) =
        if (accountType == ModelConstants.SAVING_ES) ModelConstants.SAVING else ModelConstants.CURRENT

    fun transfer(
        amount: String,
        currency: String,
        origin: String,
        accountNumber: String,
        accountType: String
    ) {
        val newFormState = validateForm(accountNumber, amount)

        if (newFormState.isValid()) {
            newFormState.isLoading = true

            viewModelScope.launch {
                val account = accountRepository.getUserAccountByAccountNumber(origin)
                val response = transferRepository.transfer(
                    amount = amount.toDouble(),
                    currency,
                    accountNumber,
                    getAccountTypeName(accountType),
                    accountOriginId = account._id
                )

                when (response) {
                    is Result.Success -> {
                        onSuccess.postValue(true)
                    }
                    is Result.Error -> {
                        errorMessage.postValue(response.exception.message)
                    }
                }
                newFormState.isLoading = false
                model.postValue(newFormState)
            }
        }

        model.postValue(newFormState)
    }
}