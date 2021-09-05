package com.example.bankapp.presentation.home.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.repository.bank.AccountRepository
import com.example.bankapp.repository.bank.EnrolledAccountRepository
import com.example.bankapp.repository.bank.TransferRepository
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.repository.bank.models.EnrolledAccount
import com.example.bankapp.repository.common.models.Result
import kotlinx.coroutines.launch

class TransferToViewModel : ViewModel() {

    private val enrolledAccountRepository = EnrolledAccountRepository()
    private val accountRepository = AccountRepository()
    private val transferRepository = TransferRepository()

    val enrolledAccount: MutableLiveData<EnrolledAccount?> by lazy {
        MutableLiveData<EnrolledAccount?>(null)
    }

    val accounts: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>(emptyList())
    }

    val errorMessageAmount: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val onSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    fun onCreate(enrolledAccountId: String) {
        viewModelScope.launch {
            val enrolled = enrolledAccountRepository.getEnrolledAccountById(enrolledAccountId)
            enrolledAccount.postValue(enrolled)

            when (val result = accountRepository.getAccounts(false)) {
                is Result.Success -> {
                    accounts.postValue(result.data.data)
                }
            }
        }
    }

    fun transfer(amount: String, currency: String, origin: String) {
        if (amount.isEmpty() || amount.toInt() < 1) {
            errorMessageAmount.postValue("Por favor ingrese un monto valido")
        } else {
            errorMessageAmount.postValue("")
            isLoading.postValue(true)
            viewModelScope.launch {
                val account = accountRepository.getUserAccountByAccountNumber(origin)
                val response = transferRepository.transfer(
                    amount = amount.toDouble(),
                    currency,
                    enrolledAccount.value!!.accountNumber,
                    enrolledAccount.value!!.accountType,
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
                isLoading.postValue(false)
            }
        }
    }
}