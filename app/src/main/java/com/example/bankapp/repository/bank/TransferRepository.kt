package com.example.bankapp.repository.bank

import com.example.bankapp.repository.bank.cloudstorage.TransferProvider
import com.example.bankapp.repository.bank.models.TransferResponse
import com.example.bankapp.repository.common.models.Result
import com.example.bankapp.repository.session.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

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
        return withContext(Dispatchers.IO) {
            val session = sessionRepository.getSession()
            val call = provider.transfer(
                session,
                amount,
                currency,
                accountDestinationNumber,
                accountDestinationType,
                accountOriginId
            )
            println("REQUEST FINISHED")
            if (call.isSuccessful) {
                val response = call.body()
                println("REQUEST OK!")
                println("RESPONSE ${response?.success}!")
                if (response?.success == true) {
                    return@withContext Result.Success(response)
                } else {
                    println("ERRORS")
                    return@withContext Result.Error(Exception(response!!.errors[0]))
                }
            }
            return@withContext Result.Error(Exception("Request fail"))
        }
    }
}