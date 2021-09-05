package com.example.bankapp.presentation.home.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.contants.ModelConstants.Companion.CURRENT
import com.example.bankapp.contants.ModelConstants.Companion.SAVING
import com.example.bankapp.contants.ModelConstants.Companion.SAVING_ES
import com.example.bankapp.repository.bank.EnrollAccountRepository
import com.example.bankapp.repository.common.models.Result
import kotlinx.coroutines.launch

class EnrollAccountViewModel : ViewModel() {
    val repository: EnrollAccountRepository = EnrollAccountRepository()

    val model: MutableLiveData<EnrollAccountForm> by lazy {
        MutableLiveData<EnrollAccountForm>(EnrollAccountForm())
    }

    val enrolledAccountSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val enrolledAccountResponse: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    private fun validateEnrollAccountForm(number: String, name: String): EnrollAccountForm {
        val newFormState = EnrollAccountForm()
        if (number.length < 11) {
            newFormState.isValidNumber = false
            newFormState.errorMessageNumber = "Número de cuenta no válido"
        } else {
            newFormState.isValidNumber = true
            newFormState.errorMessageNumber = ""
        }

        if (name.trim().length < 3) {
            newFormState.isValidName = false
            newFormState.errorMessageName = "Nombre descriptivo no válido"
        } else {
            newFormState.isValidName = true
            newFormState.errorMessageName = ""
        }
        return newFormState
    }

    private fun getAccountTypeName(accountType: String) =
        if (accountType == SAVING_ES) SAVING else CURRENT

    fun enrollAccount(accountNumber: String, name: String, accountType: String) {
        val newFormState = validateEnrollAccountForm(accountNumber, name)
        enrolledAccountResponse.postValue("")

        if (newFormState.isValid()) {
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