package com.example.bankapp.presentation.bank

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import com.example.bankapp.R
import com.example.bankapp.presentation.bank.helper.PermissionChecker
import com.example.bankapp.presentation.bank.viewModels.TransferWithQRViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.zxing.integration.android.IntentIntegrator
import com.gun0912.tedpermission.PermissionListener

class TransferWithQR : AppCompatActivity() {

    private lateinit var accounts: AppCompatSpinner
    private lateinit var amountLayout: TextInputLayout
    private lateinit var currency: TextView
    private lateinit var amount: TextInputEditText
    private lateinit var loading: ProgressBar
    private lateinit var send: Button
    private lateinit var readQR: Button
    val model: TransferWithQRViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer_with_qr)
        model.onCreate()
        setUpViewReference()
        listenViewModelUpdates()
        checkPermissions()
        setUpViewActions()
    }

    private fun setUpViewReference() {
        accounts = findViewById(R.id.accounts)
        currency = findViewById(R.id.currency)
        amountLayout = findViewById(R.id.amountLayout)
        amount = findViewById(R.id.amount)
        loading = findViewById(R.id.loading)
        send = findViewById(R.id.send)
        readQR = findViewById(R.id.read)
    }

    private fun setUpViewActions() {
        send.setOnClickListener { transfer() }
        readQR.setOnClickListener { startQRReader() }
    }

    private fun transfer() {
        model.transfer(
            amount.text.toString(),
            currency.text.toString(),
            accounts.selectedItem.toString()
        )
    }

    private fun listenViewModelUpdates() {
        model.accounts.observe(this, {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            accounts.adapter = adapter
        })

        model.amountToSend.observe(this, {
            amount.setText(it)
            amount.isEnabled = it.isEmpty()
        })

        model.currencyToSend.observe(this, {
            currency.text = it
        })

        model.amountErrorMessage.observe(this, {
            amountLayout.error = it
        })

        model.allPermissionGranted.observe(this, {
            if (it) startQRReader()
        })

        model.isLoading.observe(this, {
            send.isEnabled = !it
            loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        model.onSuccess.observe(this, {
            if (it) {
                Toast.makeText(this, "Transferencia realizada con exito", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

        model.showReadQRAction.observe(this, {
            send.visibility = if (it) View.GONE else View.VISIBLE
            readQR.visibility = if (it) View.VISIBLE else View.GONE
        })

        model.errorMessage.observe(this, {
            if (it.isNotBlank()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startQRReader() {
        IntentIntegrator(this)
            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            .setOrientationLocked(true)
            .initiateScan()
    }

    private fun checkPermissions() {
        PermissionChecker.checkAppPermissions(object : PermissionListener {
            override fun onPermissionGranted() {
                model.allPermissions(true)
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                model.allPermissions(false)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                model.setQRContent(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}