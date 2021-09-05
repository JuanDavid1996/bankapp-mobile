package com.example.bankapp.repository.bank

import com.example.bankapp.repository.bank.cloudstorage.TransferProvider
import com.example.bankapp.repository.bank.models.TransferResponse
import com.example.bankapp.network.utils.NormalizeError
import com.example.bankapp.network.utils.SafeRequest
import com.example.bankapp.network.models.Result
import com.example.bankapp.repository.session.SessionRepository

class TransferRepository {
    val sessionRepository = SessionRepository()
    private val provider: TransferProvider = TransferProvider()

    suspend fun transfer(
        amount: Double,
        currency: String,
        accountDestinationNumber: String,
        accountDestinationType: String,
        accountOriginId: String
    ): Result<TransferResponse> {
        return SafeRequest.safeRequest {
            val session = sessionRepository.getSession()
            val call = provider.transfer(
                session,
                amount,
                currency,
                accountDestinationNumber,
                accountDestinationType,
                accountOriginId
            )
            if (call.isSuccessful) {
                val response = call.body()
                if (response?.success!!) {
                    return@safeRequest Result.Success(response)
                } else {
                    return@safeRequest NormalizeError.safeError(response.errors)
                }
            }
            return@safeRequest Result.Error(Exception("Request fail"))
        }
    }
}