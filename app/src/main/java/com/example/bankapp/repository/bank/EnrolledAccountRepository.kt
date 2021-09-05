package com.example.bankapp.repository.bank

import com.example.bankapp.repository.bank.cloudstorage.EnrolledProvider
import com.example.bankapp.repository.bank.models.EnrolledAccount
import com.example.bankapp.repository.bank.models.EnrolledAccountResponse
import com.example.bankapp.network.utils.NormalizeError
import com.example.bankapp.network.utils.SafeRequest
import com.example.bankapp.repository.common.localstorage.SetUpDb.Companion.db
import com.example.bankapp.network.models.Result
import com.example.bankapp.repository.session.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class EnrolledAccountRepository {
    private val provider = EnrolledProvider()
    private val sessionRepository = SessionRepository()

    suspend fun getEnrolledAccounts(refresh: Boolean): Result<EnrolledAccountResponse> {
        return withContext(Dispatchers.IO) {
            var enrolledAccounts = db.enrolledAccountDao().getEnrolledAccount()
            if (enrolledAccounts.isEmpty() || refresh) {
                val result = getEnrolledAccountsFromCloud()
                if (result is Result.Success) {
                    enrolledAccounts = result.data
                    forceSaveEnrolledAccountInLocalStorage(enrolledAccounts)
                } else if (result is Result.Error) {
                    return@withContext result
                }
            }
            return@withContext Result.Success(
                EnrolledAccountResponse(
                    success = true,
                    data = enrolledAccounts,
                    errors = emptyList()
                )
            )
        }
    }

    private suspend fun getEnrolledAccountsFromCloud(): Result<List<EnrolledAccount>> {
        return SafeRequest.safeRequest {
            val session = sessionRepository.getSession()
            val call = provider.getEnrolledAccounts(session)
            if (call.isSuccessful) {
                val response = call.body()
                if (response?.success!!) {
                    val enrolledAccounts = response.data!!
                    return@safeRequest Result.Success(enrolledAccounts)
                } else {
                    return@safeRequest NormalizeError.safeError(response.errors)
                }
            }
            return@safeRequest Result.Error(Exception("Request fail"))
        }
    }

    private suspend fun forceSaveEnrolledAccountInLocalStorage(enrolledAccounts: List<EnrolledAccount>) {
        withContext(Dispatchers.IO) {
            removeEnrolledAccountsInLocalStorage()
            saveEnrolledAccountsInLocalStorage(enrolledAccounts)
        }
    }

    private suspend fun removeEnrolledAccountsInLocalStorage() {
        withContext(Dispatchers.IO) {
            val enrolledAccounts = db.enrolledAccountDao().getEnrolledAccount()
            if (enrolledAccounts.isNotEmpty()) enrolledAccounts.forEach {
                db.enrolledAccountDao().delete(it)
            }
        }
    }

    private suspend fun saveEnrolledAccountsInLocalStorage(enrolledAccounts: List<EnrolledAccount>) {
        withContext(Dispatchers.IO) {
            enrolledAccounts.forEach {
                db.enrolledAccountDao().insert(it)
            }
        }
    }

    suspend fun getEnrolledAccountById(enrolledAccountId: String): EnrolledAccount {
        return withContext(Dispatchers.IO) {
            return@withContext db.enrolledAccountDao().findById(enrolledAccountId)
        }
    }
}