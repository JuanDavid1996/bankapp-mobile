package com.example.bankapp.presentation.bank.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.network.models.Result
import com.example.bankapp.repository.bank.QRTransferRepository
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.utils.hasProperties
import com.example.bankapp.utils.isJson
import com.example.bankapp.utils.toJson
import com.example.bankapp.repository.bank.AccountRepository
import com.example.bankapp.utils.isNumber
import kotlinx.coroutines.launch
import org.json.JSONObject

class TransferWithQRViewModel : ViewModel() {

    private val accountRepository = AccountRepository()
    private val qrTransferRepository = QRTransferRepository()
    var transferId = ""

    val allPermissionGranted: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val onSuccess: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val showReadQRAction: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val amountToSend: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val currencyToSend: MutableLiveData<String> by lazy {
        MutableLiveData<String>("COP")
    }

    val amountErrorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val accounts: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>(emptyList())
    }


    fun onCreate() {
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts(false)) {
                is Result.Success -> {
                    accounts.postValue(result.data.data)
                }
            }
        }
    }


    fun allPermissions(allPermission: Boolean) {
        this.allPermissionGranted.postValue(allPermission)
    }

    private fun setErrorMessageAsNotValid() {
        errorMessage.postValue("QR NO VÁLIDO")
    }

    fun setQRContent(qr: String) {
        if (qr.isJson()) {
            val json = qr.toJson()
            processQRContent(json)
        } else {
            setErrorMessageAsNotValid()
        }
    }

    private fun processQRContent(json: JSONObject) {
        if (json.hasProperties(listOf("transferId", "amount", "currency"))) {
            val amount = json.getString("amount")

            if (amount.isNotEmpty() && !amount.isNumber() || amount.isNotEmpty() && amount.toInt() < 1) {
                setErrorMessageAsNotValid();
                return
            }

            amountToSend.postValue(amount)
            currencyToSend.postValue(json.getString("currency"))
            transferId = json.getString("transferId")
            showReadQRAction.postValue(false)
        } else {
            setErrorMessageAsNotValid()
        }
    }

    fun transfer(amount: String, currency: String, accountNumber: String) {
        isLoading.postValue(true)
        if (amount.isEmpty() || amount.toInt() < 1) {
            amountErrorMessage.postValue("Monto no válido")
        } else {
            isLoading.postValue(true)
            viewModelScope.launch {
                val account = accountRepository.getUserAccountByAccountNumber(accountNumber)
                when (val result =
                    qrTransferRepository.send(transferId, amount, currency, account._id)) {
                    is Result.Success -> {
                        onSuccess.postValue(true)
                    }
                    is Result.Error -> {
                        if (result.exception.message == "Transferencia no disponible") {
                            showReadQRAction.postValue(true)
                        }
                        errorMessage.postValue(result.exception.message)
                    }
                }
            }
            isLoading.postValue(false)
        }
    }

}