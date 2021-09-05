package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.EnrollAccountResponse
import com.example.bankapp.repository.bank.models.EnrolledAccountResponse
import retrofit2.Response
import retrofit2.http.*

interface EnrollService {
    @POST("/api/v1/enrolled_accounts/")
    suspend fun enrollAccount(
        @HeaderMap headers: Map<String, String>,
        @Body data: Map<String, String>
    ): Response<EnrollAccountResponse>

    @GET("/api/v1/enrolled_accounts/{userId}")
    suspend fun getEnrolledAccounts(
        @HeaderMap headers: Map<String, String>,
        @Path("userId") userId: String
    ): Response<EnrolledAccountResponse>
}