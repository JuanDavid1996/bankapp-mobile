package com.example.bankapp.presentation.session.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.repository.common.models.Result
import com.example.bankapp.repository.session.SessionRepository
import com.example.bankapp.repository.session.models.SignInResponse
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    val form: MutableLiveData<SignInForm> by lazy {
        MutableLiveData<SignInForm>(SignInForm());
    }

    val onSignInFail: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }

    val onSignInSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val repository: SessionRepository = SessionRepository();

    private fun validateForm(username: String, password: String): SignInForm {
        val newFormState = SignInForm();

        if (username.trim().isEmpty() || username.length < 3) {
            newFormState.isValidUsername = false
            newFormState.usernameMessageValidation = "Por favor ingresa un nombre de usuario válido"
        } else {
            newFormState.isValidUsername = true
            newFormState.usernameMessageValidation = ""
        }

        if (password.trim().isEmpty()) {
            newFormState.isValidPassword = false;
            newFormState.passwordMessageValidation = "Por favor ingresa la contraseña"
        } else {
            newFormState.isValidPassword = true;
            newFormState.passwordMessageValidation = ""
        }

        return newFormState;
    }

    fun doSingIn(username: String, password: String) {
        val newFormState = validateForm(username, password);

        if (newFormState.isSuccess()) {
            newFormState.isLoading = true

            viewModelScope.launch {
                val result: Result<SignInResponse?> = repository.doSignIn(username, password);
                newFormState.isLoading = false;
                form.postValue(newFormState)

                when (result) {
                    is Result.Success<*> -> {
                        val response = result.data as SignInResponse?
                        if (response?.success == true) {
                            onSignInSuccess.postValue(true)
                        }
                    }
                    is Result.Error -> {
                        println("Fallo ? ${result.exception}")
                        println("Fallo ? ${result.exception.message}")
                        onSignInFail.postValue(result.exception.message)
                    }
                }
            }
        }

        form.postValue(newFormState)
    }
}