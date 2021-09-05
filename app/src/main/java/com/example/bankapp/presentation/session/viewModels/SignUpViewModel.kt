package com.example.bankapp.presentation.session.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.network.models.Result
import com.example.bankapp.repository.session.SessionRepository
import com.example.bankapp.repository.session.models.SignUpResponse
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    val form: MutableLiveData<SignUpForm> by lazy {
        MutableLiveData<SignUpForm>(SignUpForm())
    }

    val onSignUpFail: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }

    val onSignUpSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }

    val repository: SessionRepository = SessionRepository()

    private fun validateForm(
        firstName: String,
        lastName: String,
        identification: String,
        username: String,
        password: String,
        passwordConfirmation: String
    ): SignUpForm {
        val newFormState = SignUpForm()

        if (firstName.trim().isEmpty() || firstName.length < 3) {
            newFormState.isValidFirstName = false
            newFormState.firstNameMessageValidation = "Por favor ingresa un nombre válido"
        } else {
            newFormState.isValidFirstName = true
            newFormState.firstNameMessageValidation = ""
        }

        if (lastName.trim().isEmpty() || lastName.length < 3) {
            newFormState.isValidLastName = false
            newFormState.lastNameMessageValidation = "Por favor ingresa un apellido válido"
        } else {
            newFormState.isValidLastName = true
            newFormState.lastNameMessageValidation = ""
        }

        if (identification.trim().isEmpty() || identification.length < 6) {
            newFormState.isValidIdentification = false
            newFormState.identificationMessageValidation = "Por favor ingresa una identicicación válida"
        } else {
            newFormState.isValidIdentification = true
            newFormState.identificationMessageValidation = ""
        }

        if (username.trim().isEmpty() || username.length < 3) {
            newFormState.isValidUsername = false
            newFormState.usernameMessageValidation = "Por favor ingresa un nombre de usuario válido"
        } else {
            newFormState.isValidUsername = true
            newFormState.usernameMessageValidation = ""
        }

        if (password.trim().isEmpty()) {
            newFormState.isValidPassword = false
            newFormState.passwordMessageValidation = "Por favor ingresa la contraseña"
        } else {
            newFormState.isValidPassword = true
            newFormState.passwordMessageValidation = ""
        }

        if (passwordConfirmation.trim().isEmpty()) {
            newFormState.isValidPasswordConfirmation = false
            newFormState.passwordConfirmationMessageValidation = "Por favor ingresa la contraseña"
        } else {
            newFormState.isValidPasswordConfirmation = true
            newFormState.passwordConfirmationMessageValidation = ""
        }

        if (newFormState.isValidPassword && newFormState.isValidPasswordConfirmation) {
            if (password != passwordConfirmation) {
                newFormState.isValidPassword = false
                newFormState.passwordMessageValidation =
                    "Contraseña y confirmacion de contraseña no son iguales"

                newFormState.isValidPasswordConfirmation = false
                newFormState.passwordConfirmationMessageValidation =
                    "Contraseña y confirmacion de contraseña no son iguales"
            }
        }

        return newFormState
    }

    fun doSignUp(
        firstName: String,
        lastName: String,
        identification: String,
        username: String,
        password: String,
        passwordConfirmation: String
    ) {
        val newFormState = validateForm(
            firstName,
            lastName,
            identification,
            username,
            password,
            passwordConfirmation
        )

        if (newFormState.isSuccess()) {
            newFormState.isLoading = true

            viewModelScope.launch {
                val result: Result<SignUpResponse?> =
                    repository.doSignUp(firstName, lastName, identification, username, password)
                newFormState.isLoading = false
                form.postValue(newFormState)

                when (result) {
                    is Result.Success<*> -> {
                        val response = result.data as SignUpResponse?
                        if (response?.success == true) {
                            onSignUpSuccess.postValue(true)
                        }
                    }
                    is Result.Error -> {
                        println("Fallo ? ${result.exception}")
                        println("Fallo ? ${result.exception.message}")
                        onSignUpFail.postValue(result.exception.message)
                    }
                }
            }
        }

        form.postValue(newFormState)
    }
}