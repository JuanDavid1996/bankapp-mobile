package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.EnrolledAccountResponse
import com.example.bankapp.repository.common.AppProvider
import com.example.bankapp.repository.session.models.Session
import retrofit2.Response


class EnrolledProvider : AppProvider() {
    suspend fun getEnrolledAccounts(
        session: Session
    ): Response<EnrolledAccountResponse> {

        return getRetrofit().create(EnrollService::class.java)
            .getEnrolledAccounts(NetworkSecurityHelper.buildAuthHeaders(session), session.userId)
    }
}