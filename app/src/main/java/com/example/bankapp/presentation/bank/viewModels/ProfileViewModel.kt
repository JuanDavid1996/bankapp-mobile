package com.example.bankapp.presentation.bank.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.repository.session.SessionRepository
import com.example.bankapp.repository.session.models.User
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    val onLogOut: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val user: MutableLiveData<User?> by lazy {
        MutableLiveData(null)
    }

    val repository: SessionRepository = SessionRepository()

    fun onCreate() {
        viewModelScope.launch {
            user.postValue(repository.getCurrentUser())
        }
    }

    fun logOut() {
        viewModelScope.launch {
            repository.logOut()
            onLogOut.postValue(true);
        }
    }
}