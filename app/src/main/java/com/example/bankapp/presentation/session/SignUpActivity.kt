package com.example.bankapp.presentation.session

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bankapp.R
import com.example.bankapp.presentation.session.viewModels.SignUpViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var firstNameLayout: TextInputLayout
    private lateinit var lastNameLayout: TextInputLayout
    private lateinit var identificationLayout: TextInputLayout
    private lateinit var usernameLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordConfirmationLayout: TextInputLayout

    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var identification: TextInputEditText
    private lateinit var username: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var passwordConfirmation: TextInputEditText

    private lateinit var loading: ProgressBar
    private lateinit var signUp: Button

    private val model: SignUpViewModel by viewModels();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setUpViewReferences()
        setUpButtonActions()
        listenViewModelUpdates()
    }

    private fun setUpViewReferences() {
        firstNameLayout = findViewById(R.id.firstNameLayout)
        firstName = findViewById(R.id.firstName)

        lastNameLayout = findViewById(R.id.lastNameLayout)
        lastName = findViewById(R.id.lastName)

        identificationLayout = findViewById(R.id.identificationLayout)
        identification = findViewById(R.id.identification)

        usernameLayout = findViewById(R.id.layoutUsername)
        username = findViewById(R.id.username)

        passwordLayout = findViewById(R.id.layoutPassword)
        password = findViewById(R.id.password)

        passwordConfirmationLayout = findViewById(R.id.layoutPasswordConfirmation)
        passwordConfirmation = findViewById(R.id.passwordConfirmation)

        loading = findViewById(R.id.loading)
        signUp = findViewById(R.id.signUp)
    }

    private fun setUpButtonActions() {
        signUp.setOnClickListener(this)
    }

    private fun listenViewModelUpdates() {
        model.form.observe(this, {

            if (it.isValidFirstName) {
                firstNameLayout.error = null
            } else {
                firstNameLayout.error = it.firstNameMessageValidation
            }

            if (it.isValidLastName) {
                lastNameLayout.error = null
            } else {
                lastNameLayout.error = it.lastNameMessageValidation
            }

            if (it.isValidIdentification) {
                identificationLayout.error = null
            } else {
                identificationLayout.error = it.identificationMessageValidation
            }

            if (it.isValidUsername) {
                usernameLayout.error = null
            } else {
                usernameLayout.error = it.usernameMessageValidation
            }

            if (it.isValidPassword) {
                passwordLayout.error = null
            } else {
                passwordLayout.error = it.passwordMessageValidation
            }

            loading.visibility = if (it.isLoading) View.VISIBLE else View.GONE
            signUp.isEnabled = !it.isLoading
        })

        model.onSignUpSuccess.observe(this, {
            if (it) {
                Toast.makeText(this, "Registrado existosamente", Toast.LENGTH_SHORT).show();
                toSignIn()
            }
        })

        model.onSignUpFail.observe(this, {
            if (it.isNotBlank()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show();
            }
        })
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.signUp) doSignUp()
    }

    private fun doSignUp() {
        model.doSignUp(
            firstName = firstName.text.toString(),
            lastName = lastName.text.toString(),
            identification = identification.text.toString(),
            username = username.text.toString(),
            password = password.text.toString(),
            passwordConfirmation = passwordConfirmation.text.toString()
        )
    }

    private fun toSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}