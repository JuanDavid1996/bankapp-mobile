package com.example.bankapp.presentation.bank

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.bankapp.R
import com.example.bankapp.presentation.bank.viewModels.GenerateTransferByQRViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception


class GenerateTransferByQR : AppCompatActivity() {

    private lateinit var accounts: AppCompatSpinner
    private lateinit var amountLayout: TextInputLayout
    private lateinit var currency: AppCompatSpinner
    private lateinit var amount: TextInputEditText
    private lateinit var loading: ProgressBar
    private lateinit var create: Button
    private lateinit var qr: ImageView
    private val model: GenerateTransferByQRViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_transfer_by_qr)
        setUpViewReference()
        setUpViewActions()
        listenViewModelUpdates()
        model.onCreate()
    }

    private fun setUpViewReference() {
        accounts = findViewById(R.id.accounts)
        currency = findViewById(R.id.currency)
        amountLayout = findViewById(R.id.amountLayout)
        amount = findViewById(R.id.amount)
        loading = findViewById(R.id.loading)
        create = findViewById(R.id.create)
        qr = findViewById(R.id.qr)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("COP", "USD")
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        currency.adapter = adapter
    }

    private fun setUpViewActions() {
        create.setOnClickListener {
            model.create(
                amount.text.toString(),
                currency.selectedItem.toString(),
                accounts.selectedItem.toString()
            )
        }
    }

    private fun listenViewModelUpdates() {
        model.isLoading.observe(this, {
            loading.visibility = if (it) View.VISIBLE else View.GONE
            create.isEnabled = !it
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

        model.qr.observe(this, {
            if (it.isNotBlank()) {
                try {
                    println("RECEIVED $it")
                    val decodedString: ByteArray =
                        Base64.decode(it.replace("data:image/png;base64,", ""), Base64.DEFAULT)
                    val decodedByte =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    qr.setImageBitmap(decodedByte)
                } catch (e: Exception) {
                    println("Error $e")
                }
            }
        })

        model.errorMessageAmount.observe(this, {
            amountLayout.error = it
        })
    }

}