package com.example.bankapp.repository.bank.cloudstorage

import com.example.bankapp.repository.bank.models.QRTransferResponse
import com.example.bankapp.repository.bank.models.EnrolledAccountResponse
import retrofit2.Response
import retrofit2.http.*

interface QRTransferService {
    @POST("/api/v1/qr_transfers/")
    suspend fun create(
        @HeaderMap headers: Map<String, String>,
        @Body data: Map<String, String>
    ): Response<QRTransferResponse>

    @PUT("/api/v1/qr_transfers/{transfer_id}")
    suspend fun send(
        @HeaderMap headers: Map<String, String>,
        @Path("transfer_id") transferId: String,
    ): Response<EnrolledAccountResponse>
}