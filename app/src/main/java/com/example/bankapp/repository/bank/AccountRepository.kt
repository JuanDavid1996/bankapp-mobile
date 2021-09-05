package com.example.bankapp.repository.bank

import com.example.bankapp.repository.bank.cloudstorage.AccountProvider
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.repository.bank.models.AccountResponse
import com.example.bankapp.repository.common.localstorage.SetUpDb.Companion.db
import com.example.bankapp.repository.common.models.Result
import com.example.bankapp.repository.session.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AccountRepository {
    private val provider = AccountProvider()
    private val sessionRepository = SessionRepository()

    suspend fun getAccounts(refresh: Boolean): Result<AccountResponse> {
        return withContext(Dispatchers.IO) {
            var accounts = db.accountDao().getAccounts()
            if (accounts.isEmpty() || refresh) {
                val result = getAccountsFromCloud()
                if (result is Result.Error) {
                    return@withContext result
                } else if (result is Result.Success) {
                    accounts = result.data
                    forceSaveAccountsInLocalStorage(accounts)
                }
            }
            return@withContext Result.Success(
                AccountResponse(
                    success = true,
                    data = accounts,
                    errors = emptyList()
                )
            )
        }
    }

    private suspend fun getAccountsFromCloud(): Result<List<Account>> {
        return withContext(Dispatchers.IO) {
            val session = sessionRepository.getSession();
            val call = provider.getAccounts(session)
            val response = call.body()
            if (call.isSuccessful) {
                if (response?.success == true) {
                    return@withContext Result.Success(response.data!!)
                } else {
                    return@withContext Result.Error(Exception(response!!.errors[0]))
                }
            }
            return@withContext Result.Error(Exception("Failed to fetch"))
        }
    }

    private suspend fun forceSaveAccountsInLocalStorage(accounts: List<Account>) {
        withContext(Dispatchers.IO) {
            deleteAccountsInLocalStorageIfNeeded(accounts)
            saveAccountsInLocalStorage(accounts)
        }
    }

    private suspend fun saveAccountsInLocalStorage(accounts: List<Account>) {
        withContext(Dispatchers.IO) {
            accounts.forEach { db.accountDao().insert(it) }
        }
    }

    private suspend fun deleteAccountsInLocalStorageIfNeeded(accounts: List<Account>) {
        withContext(Dispatchers.IO) {
            if (accounts.isNotEmpty()) {
                accounts.forEach { db.accountDao().delete(it) }
            }
        }
    }

    suspend fun getUserAccountByAccountNumber(origin: String): Account {
        return withContext(Dispatchers.IO) {
            return@withContext db.accountDao().getAccountByAccountNumber(origin)
        }
    }
}