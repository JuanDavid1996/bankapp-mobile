package com.example.bankapp.presentation.bank.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.repository.bank.AccountRepository
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.network.models.Result
import kotlinx.coroutines.launch

class AccountsViewModel : ViewModel() {
    val repository = AccountRepository()

    val accounts: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>(emptyList())
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val onErrorMessage: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }

    fun getAccounts(refresh: Boolean) {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (val result = repository.getAccounts(refresh)) {
                is Result.Success -> {
                    accounts.postValue(result.data.data)
                }
                is Result.Error -> {
                    onErrorMessage.postValue(result.exception.message)
                }
            }
            isLoading.postValue(false)
        }
    }
}