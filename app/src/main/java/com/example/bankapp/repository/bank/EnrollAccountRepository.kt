package com.example.bankapp.repository.bank

import com.example.bankapp.repository.bank.cloudstorage.EnrollProvider
import com.example.bankapp.repository.bank.models.EnrollAccountResponse
import com.example.bankapp.repository.bank.models.EnrolledAccount
import com.example.bankapp.repository.common.cloudstorage.NormalizeError
import com.example.bankapp.repository.common.cloudstorage.SafeRequest
import com.example.bankapp.repository.common.localstorage.SetUpDb.Companion.db
import com.example.bankapp.repository.common.models.Result
import com.example.bankapp.repository.session.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EnrollAccountRepository {
    private val provider = EnrollProvider()
    private val sessionRepository = SessionRepository()

    suspend fun enrollAccount(
        accountNumber: String,
        name: String,
        accountType: String
    ): Result<EnrollAccountResponse> {
        return SafeRequest.safeRequest {
            val session = sessionRepository.getSession()
            val call = provider.enrollAccount(session, accountNumber, name, accountType)
            if (call.isSuccessful) {
                val response = call.body()
                if (response?.success!!) {
                    saveEnrolledAccountInLocalStorage(response.data!!)
                    return@safeRequest Result.Success(response)
                } else {
                    return@safeRequest NormalizeError.safeError(response.errors)
                }
            }
            return@safeRequest Result.Error(Exception("Request fail"))
        }
    }

    private suspend fun saveEnrolledAccountInLocalStorage(enrolledAccount: EnrolledAccount) {
        return withContext(Dispatchers.IO) {
            db.enrolledAccountDao().insert(enrolledAccount)
        }
    }
}