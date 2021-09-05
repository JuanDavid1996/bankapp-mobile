package com.example.bankapp.repository.bank

import com.example.bankapp.network.models.Result
import com.example.bankapp.network.utils.NormalizeError
import com.example.bankapp.network.utils.SafeRequest
import com.example.bankapp.repository.bank.cloudstorage.QRTransferProvider
import com.example.bankapp.repository.bank.models.QRTransferResponse
import com.example.bankapp.repository.bank.models.TransferResponse
import com.example.bankapp.repository.session.SessionRepository
import java.lang.Exception

class QRTransferRepository {
    private val sessionRepository = SessionRepository()
    private val provider = QRTransferProvider()

    suspend fun create(
        accountDestinationId: String,
        amount: String,
        currency: String
    ): Result<QRTransferResponse> {
        return SafeRequest.safeRequest {
            val session = sessionRepository.getSession()
            val call = provider.create(
                session,
                accountDestinationId = accountDestinationId,
                amount = amount,
                currency = currency
            )
            if (call.isSuccessful) {
                val response = call.body()
                if (response!!.success) {
                    return@safeRequest Result.Success(response)
                } else {
                    return@safeRequest NormalizeError.safeError(response.errors)
                }
            }
            return@safeRequest Result.Error(Exception("Fail request"))
        }
    }

    suspend fun send(
        transferId: String,
        amount: String,
        currency: String,
        accountOriginId: String
    ): Result<TransferResponse> {
        return SafeRequest.safeRequest {
            val session = sessionRepository.getSession()
            val call = provider.send(
                session,
                accountOriginId = accountOriginId,
                amount = amount,
                currency = currency,
                transferId = transferId
            )
            if (call.isSuccessful) {
                val response = call.body()
                if (response!!.success) {
                    return@safeRequest Result.Success(response)
                } else {
                    return@safeRequest NormalizeError.safeError(response.errors)
                }
            }
            return@safeRequest Result.Error(Exception("Fail request"))
        }
    }
}