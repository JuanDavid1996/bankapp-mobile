package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.QRTransferResponse
import com.example.bankapp.repository.common.AppProvider
import com.example.bankapp.repository.session.models.Session
import retrofit2.Response

class QRTransferProvider : AppProvider() {
    suspend fun create(
        session: Session,
        accountDestinationId: String,
        amount: String,
        currency: String
    ): Response<QRTransferResponse> {
        val data = HashMap<String, String>()
        data["accountDestinationId"] = accountDestinationId
        data["amount"] = amount
        data["currency"] = currency
        return getRetrofit().create(QRTransferService::class.java).create(
            NetworkSecurityHelper.buildAuthHeaders(session), data
        )
    }
}