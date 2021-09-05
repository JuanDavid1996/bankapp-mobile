package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.TransferResponse
import com.example.bankapp.repository.common.AppProvider
import com.example.bankapp.repository.session.models.Session
import retrofit2.Response

class TransferProvider : AppProvider() {
    suspend fun transfer(
        session: Session,
        amount: Double,
        currency: String,
        accountDestinationNumber: String,
        accountDestinationType: String,
        accountOriginId: String,
    ): Response<TransferResponse> {
        val data = HashMap<String, String>()
        data["amount"] = amount.toString()
        data["currency"] = currency
        data["accountDestinationNumber"] = accountDestinationNumber
        data["accountDestinationType"] = accountDestinationType
        data["accountOriginId"] = accountOriginId
        return getRetrofit().create(TransferService::class.java)
            .createTransfer(NetworkSecurityHelper.buildAuthHeaders(session), data)
    }
}