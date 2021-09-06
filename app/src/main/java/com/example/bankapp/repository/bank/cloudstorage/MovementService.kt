package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.MovementResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface MovementService {
    @GET("/api/v1/movements/{accountId}")
    suspend fun getMovements(
        @HeaderMap headers: Map<String, String>,
        @Path("accountId") accountId: String
    ): Response<MovementResponse>
}