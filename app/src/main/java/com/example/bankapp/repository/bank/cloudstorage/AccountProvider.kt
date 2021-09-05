package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.AccountResponse
import com.example.bankapp.repository.common.AppProvider
import com.example.bankapp.repository.session.models.Session
import retrofit2.Response

class AccountProvider : AppProvider() {
    suspend fun getAccounts(session: Session): Response<AccountResponse> {
        return getRetrofit().create(AccountService::class.java)
            .getAccounts(NetworkSecurityHelper.buildAuthHeaders(session), session.userId)
    }
}