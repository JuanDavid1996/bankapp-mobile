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
import com.example.bankapp.presentation.bank.HomeActivity
import com.example.bankapp.presentation.session.viewModels.SignInViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

//TODO: MOVE ALL HARD CODE TEXT TO STRING.XML

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var usernameLayout: TextInputLayout
    private lateinit var username: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var password: TextInputEditText
    private lateinit var loading: ProgressBar
    private lateinit var sigIn: Button
    private lateinit var signUp: Button

    private val model: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        setUpViewReferences()
        setUpButtonActions()
        listenViewModelUpdates()
    }

    private fun setUpViewReferences() {
        usernameLayout = findViewById(R.id.layoutUsername)
        username = findViewById(R.id.username)
        passwordLayout = findViewById(R.id.layoutPassword)
        password = findViewById(R.id.password)
        loading = findViewById(R.id.loading)
        sigIn = findViewById(R.id.signIn)
        signUp = findViewById(R.id.signUp)
    }

    private fun setUpButtonActions() {
        sigIn.setOnClickListener(this)
        signUp.setOnClickListener(this)
    }

    private fun listenViewModelUpdates() {
        model.form.observe(this, {
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
            sigIn.isEnabled = !it.isLoading
            signUp.isEnabled = !it.isLoading
        })

        model.onSignInSuccess.observe(this, {
            if (it) toHome()
        })

        model.onSignInFail.observe(this, {
            if (it.isNotBlank()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.signIn) doSignIn()
        else if (v?.id == R.id.signUp) toSignUp()
    }

    private fun doSignIn() {
        model.doSingIn(username.text.toString(), password.text.toString())
    }

    private fun toSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun toHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}