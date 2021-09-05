package com.example.bankapp.presentation.home.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.repository.session.SessionRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    val onLogOut: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val repository: SessionRepository = SessionRepository();

    fun logOut() {
        viewModelScope.launch {
            repository.logOut()
            onLogOut.postValue(true);
        }
    }
}