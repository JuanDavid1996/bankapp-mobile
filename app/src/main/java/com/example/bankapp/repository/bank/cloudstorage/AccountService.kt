package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.AccountResponse
import retrofit2.Response
import retrofit2.http.*

interface AccountService {
    @GET("/api/v1/accounts/{userId}")
    suspend fun getAccounts(
        @HeaderMap headerMap: Map<String, String>,
        @Path("userId") userId: String
    ): Response<AccountResponse>

    @POST("/api/v1/accounts/")
    suspend fun createAccount(
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Map<String, String>
    ): Response<AccountResponse>
}