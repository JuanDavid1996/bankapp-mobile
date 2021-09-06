package com.example.bankapp.repository.bank.cloudstorage


import com.example.bankapp.repository.bank.models.MovementResponse
import com.example.bankapp.repository.common.AppProvider
import com.example.bankapp.repository.session.models.Session
import retrofit2.Response

class MovementProvider : AppProvider() {
    suspend fun getMovements(session: Session, accountId: String): Response<MovementResponse> {
        return getRetrofit().create(MovementService::class.java).getMovements(
            NetworkSecurityHelper.buildAuthHeaders(session), accountId
        )
    }
}