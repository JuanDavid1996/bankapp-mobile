package com.example.bankapp.presentation.bank

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
import com.example.bankapp.contants.ViewRouterParams
import com.example.bankapp.presentation.bank.viewModels.TransferToViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class TransferTo : AppCompatActivity() {

    private lateinit var accounts: AppCompatSpinner
    private lateinit var accountDestination: TextInputEditText
    private lateinit var amountLayout: TextInputLayout
    private lateinit var currency: AppCompatSpinner
    private lateinit var amount: TextInputEditText
    private lateinit var loading: ProgressBar
    private lateinit var transfer: Button
    private val model: TransferToViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_to)
        setUpViewReference()
        setUpViewActions()
        listenViewModelUpdates()

        val enrolledAccountId = intent.extras!!.getString(ViewRouterParams.ENROLLED_ACCOUNT_ID)!!
        model.onCreate(enrolledAccountId)
    }

    private fun setUpViewReference() {
        accountDestination = findViewById(R.id.accountDestination)
        accounts = findViewById(R.id.accountOrigins)
        currency = findViewById(R.id.currency)
        amountLayout = findViewById(R.id.amountLayout)
        amount = findViewById(R.id.amount)
        loading = findViewById(R.id.loading)
        transfer = findViewById(R.id.transfer)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("COP", "USD")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currency.adapter = adapter
    }

    private fun setUpViewActions() {
        transfer.setOnClickListener {
            model.transfer(
                amount.text.toString(),
                currency.selectedItem.toString(),
                accounts.selectedItem.toString(),
            )
        }
    }

    private fun listenViewModelUpdates() {
        model.enrolledAccount.observe(this, {
            if (it != null) {
                accountDestination.setText(it.accountNumber)
            }
        })

        model.accounts.observe(this, {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            accounts.adapter = adapter
        })

        model.errorMessageAmount.observe(this, {
            if (it.isNotBlank()) {
                amountLayout.error = it
            } else {
                amountLayout.error = null
            }
        })

        model.errorMessage.observe(this, {
            if (it.isNotBlank()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        model.isLoading.observe(this, {
            loading.visibility = if (it) View.VISIBLE else View.GONE
            transfer.isEnabled = !it
        })

        model.onSuccess.observe(this, {
            if (it) {
                Toast.makeText(this, "Transferencia realizada", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }
}