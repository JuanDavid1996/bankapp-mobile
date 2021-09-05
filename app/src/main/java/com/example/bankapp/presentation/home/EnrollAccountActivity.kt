package com.example.bankapp.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatSpinner
import com.example.bankapp.R
import com.example.bankapp.presentation.home.viewModels.EnrollAccountViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EnrollAccountActivity : AppCompatActivity() {

    private lateinit var accountNumberLayout: TextInputLayout
    private lateinit var accountNumber: TextInputEditText
    private lateinit var nameLayout: TextInputLayout
    private lateinit var name: TextInputEditText
    private lateinit var accountTypes: AppCompatSpinner
    private lateinit var loading: ProgressBar
    private lateinit var create: Button

    private val model: EnrollAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_account)
        setUpViewReferences()
        setUpViewActions()
        listenViewModelUpdates()
    }

    private fun setUpViewReferences() {
        accountNumberLayout = findViewById(R.id.accountNumberLayout)
        accountNumber = findViewById(R.id.accountNumber)
        nameLayout = findViewById(R.id.nameLayout)
        name = findViewById(R.id.name)
        accountTypes = findViewById(R.id.accountTypes)
        loading = findViewById(R.id.loading)
        create = findViewById(R.id.create)

        val accountTypesAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.account_types,
            android.R.layout.simple_spinner_dropdown_item
        )

        accountTypes.adapter = accountTypesAdapter
    }

    private fun setUpViewActions() {
        create.setOnClickListener {
            model.enrollAccount(
                accountNumber = accountNumber.text.toString(),
                name = accountNumber.text.toString(),
                accountType = accountTypes.selectedItem.toString(),
            )
        }
    }

    private fun listenViewModelUpdates() {
        model.model.observe(this, {
            if (it.isValidNumber) {
                accountNumberLayout.error = null
            } else {
                accountNumberLayout.error = it.errorMessageNumber
            }

            loading.visibility = if (it.isLoading) View.VISIBLE else View.GONE
            create.isEnabled = !it.isLoading
        })

        model.enrolledAccountSuccess.observe(this, {
            if (it) finish()
        })

        model.enrolledAccountResponse.observe(this, {
            if (it.isNotBlank()) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }
}