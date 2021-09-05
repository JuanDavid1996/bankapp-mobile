package com.example.bankapp.presentation.home.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.repository.bank.EnrolledAccountRepository
import com.example.bankapp.repository.bank.models.EnrolledAccount
import com.example.bankapp.repository.common.models.Result
import kotlinx.coroutines.launch

class EnrolledAccountsViewModel : ViewModel() {
    val repository = EnrolledAccountRepository()

    val enrolledAccounts: MutableLiveData<List<EnrolledAccount>> by lazy {
        MutableLiveData<List<EnrolledAccount>>(emptyList())
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val onErrorMessage: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }

    fun getEnrolledAccounts(refresh: Boolean = false) {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (val result = repository.getEnrolledAccounts(refresh)) {
                is Result.Success -> {
                    enrolledAccounts.postValue(result.data.data)
                }
                is Result.Error -> {
                    onErrorMessage.postValue(result.exception.message)
                }
            }
            isLoading.postValue(false)
        }
    }
}