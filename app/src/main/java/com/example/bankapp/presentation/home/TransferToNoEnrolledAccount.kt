package com.example.bankapp.presentation.home

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.bankapp.R
import com.example.bankapp.presentation.home.viewModels.TransferToNotEnrolledAccountViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class TransferToNoEnrolledAccount : AppCompatActivity() {


    private lateinit var accounts: AppCompatSpinner
    private lateinit var accountDestinationLayout: TextInputLayout
    private lateinit var accountDestination: TextInputEditText
    private lateinit var amountLayout: TextInputLayout
    private lateinit var currency: AppCompatSpinner
    private lateinit var amount: TextInputEditText
    private lateinit var loading: ProgressBar
    private lateinit var transfer: Button
    private lateinit var accountTypes: AppCompatSpinner

    private val model: TransferToNotEnrolledAccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_to_no_enrolled_account)
        setUpViewReference()
        setUpViewActions()
        listenViewModelUpdates()
        model.onCreate()
    }

    private fun setUpViewReference() {
        accountDestinationLayout = findViewById(R.id.accountDestinationLayout)
        accountDestination = findViewById(R.id.accountDestination)
        accounts = findViewById(R.id.accountOrigins)
        currency = findViewById(R.id.currency)
        amountLayout = findViewById(R.id.amountLayout)
        amount = findViewById(R.id.amount)
        loading = findViewById(R.id.loading)
        transfer = findViewById(R.id.transfer)
        accountTypes = findViewById(R.id.accountTypes)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("COP", "USD")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currency.adapter = adapter

        val accountTypesAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.account_types,
            android.R.layout.simple_spinner_dropdown_item
        )
        accountTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        accountTypes.adapter = accountTypesAdapter
    }

    private fun setUpViewActions() {
        transfer.setOnClickListener {
            model.transfer(
                amount.text.toString(),
                currency.selectedItem.toString(),
                accounts.selectedItem.toString(),
                accountNumber = accountDestination.text.toString(),
                accountType = accountTypes.selectedItem.toString()
            )
        }
    }

    private fun listenViewModelUpdates() {
        model.model.observe(this, {
            if (it.isValidNumber) {
                accountDestinationLayout.error = null
            } else {
                accountDestinationLayout.error = it.errorMessageNumber
            }

            if (it.isValidAmount) {
                amountLayout.error = null
            } else {
                amountLayout.error = it.errorMessageAmount
            }

            loading.visibility = if (it.isLoading) View.VISIBLE else View.GONE
            transfer.isEnabled = !it.isLoading
        })

        model.accounts.observe(this, {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            accounts.adapter = adapter
        })

        model.errorMessage.observe(this, {
            if (it.isNotBlank()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })


        model.onSuccess.observe(this, {
            if (it) {
                Toast.makeText(this, "Transferencia realizada", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}