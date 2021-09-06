package com.example.bankapp.presentation.bank.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.network.models.Result
import com.example.bankapp.repository.bank.AccountRepository
import com.example.bankapp.repository.bank.MovementRepository
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.repository.bank.models.Movement
import kotlinx.coroutines.launch

class EconomicMovementsViewModel : ViewModel() {

    private val accountRepository = AccountRepository()
    private val movementRepository = MovementRepository()
    private var accountId: String = ""

    val account: MutableLiveData<Account?> by lazy {
        MutableLiveData<Account?>(null)
    }

    val movements: MutableLiveData<List<Movement>> by lazy {
        MutableLiveData<List<Movement>>(emptyList())
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val onErrorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    fun onCreate(accountId: String) {
        this.accountId = accountId
        viewModelScope.launch { account.postValue(accountRepository.getAccountById(accountId)) }
        onRefresh()
    }

    fun onRefresh() {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (val result = movementRepository.getMovements(accountId, true)) {
                is Result.Success -> {
                    movements.postValue(result.data)
                }
                is Result.Error -> {
                    onErrorMessage.postValue(result.exception.message)
                }
            }
            isLoading.postValue(false)
        }
    }
}