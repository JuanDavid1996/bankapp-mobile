package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.TransferResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface TransferService {
    @POST("/api/v1/transfers")
    suspend fun createTransfer(@HeaderMap headers: Map<String, String>, @Body data: Map<String, String>): Response<TransferResponse>
}