package com.example.bankapp.presentation.bank.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.network.models.Result
import com.example.bankapp.repository.bank.AccountRepository
import com.example.bankapp.repository.bank.QRTransferRepository
import com.example.bankapp.repository.bank.models.Account
import kotlinx.coroutines.launch

class GenerateTransferByQRViewModel : ViewModel() {
    private val accountRepository = AccountRepository()
    private val qrRepository = QRTransferRepository()

    val accounts: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>(emptyList())
    }

    val errorMessageAmount: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val qr: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
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

    fun create(amount: String, currency: String, destination: String) {
        if (amount.isNotBlank() && amount.toInt() < 1) {
            errorMessageAmount.postValue("Por favor ingresa un monto superior a cero")
        } else {
            errorMessageAmount.postValue("");
            isLoading.postValue(true)
            viewModelScope.launch {
                val account = accountRepository.getUserAccountByAccountNumber(destination)
                when (val result =
                    qrRepository.create(accountDestinationId = account._id, amount, currency)) {
                    is Result.Success -> {
                        qr.postValue(result.data.data)
                    }
                    is Result.Error -> {
                        errorMessage.postValue(result.exception.message)
                    }
                }
                isLoading.postValue(false)
            }
        }
    }
}