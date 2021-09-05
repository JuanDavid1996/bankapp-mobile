package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.EnrollAccountResponse
import com.example.bankapp.repository.common.AppProvider
import com.example.bankapp.repository.session.models.Session
import retrofit2.Response


class EnrollProvider : AppProvider() {
    suspend fun enrollAccount(
        session: Session,
        accountNumber: String,
        name: String,
        accountType: String
    ): Response<EnrollAccountResponse> {

        val data = HashMap<String, String>()
        data["accountNumber"] = accountNumber
        data["name"] = name
        data["accountType"] = accountType

        return getRetrofit().create(EnrollService::class.java)
            .enrollAccount(NetworkSecurityHelper.buildAuthHeaders(session), data)
    }
}