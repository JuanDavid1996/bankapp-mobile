package com.example.bankapp.presentation.home.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.contants.ModelConstants.Companion.CURRENT
import com.example.bankapp.contants.ModelConstants.Companion.SAVING
import com.example.bankapp.contants.ModelConstants.Companion.SAVING_ES
import com.example.bankapp.repository.bank.EnrolledAccountRepository
import com.example.bankapp.repository.common.models.Result
import kotlinx.coroutines.launch

class EnrollAccountViewModel : ViewModel() {
    val repository: EnrolledAccountRepository = EnrolledAccountRepository()

    val model: MutableLiveData<RegisterAccountForm> by lazy {
        MutableLiveData<RegisterAccountForm>(RegisterAccountForm())
    }

    val enrolledAccountSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val enrolledAccountResponse: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    private fun validateEnrollAccountForm(number: String): RegisterAccountForm {
        val newFormState = RegisterAccountForm()
        if (number.length < 11) {
            newFormState.isValidNumber = false
            newFormState.errorMessageNumber = "Número de cuenta no válido"
        } else {
            newFormState.isValidNumber = true
            newFormState.errorMessageNumber = ""
        }
        return newFormState
    }

    private fun getAccountTypeName(accountType: String) =
        if (accountType == SAVING_ES) SAVING else CURRENT

    fun enrollAccount(accountNumber: String, name: String, accountType: String) {
        val newFormState = validateEnrollAccountForm(accountNumber)
        enrolledAccountResponse.postValue("")

        if (newFormState.isValidNumber) {
            newFormState.isLoading = true
            viewModelScope.launch {
                when (val result = repository.enrollAccount(
                    accountNumber,
                    name,
                    getAccountTypeName(accountType)
                )) {
                    is Result.Success -> {
                        enrolledAccountResponse.postValue("Cuenta registrada existosamente")
                        enrolledAccountSuccess.postValue(true)
                    }
                    is Result.Error -> {
                        enrolledAccountResponse.postValue(result.exception.message)
                    }
                }

                newFormState.isLoading = false
                model.postValue(newFormState)
            }
        }

        model.postValue(newFormState)
    }
}