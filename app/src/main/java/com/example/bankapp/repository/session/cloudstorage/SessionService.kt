package com.example.bankapp.repository.session.cloudstorage

import com.example.bankapp.repository.session.models.SignInResponse
import com.example.bankapp.repository.session.models.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SessionService {

    @POST("/api/v1/session/signIn/")
    suspend fun doSigIn(@Body body: Map<String, String>): Response<SignInResponse>

    @POST("/api/v1/session/signUp/")
    suspend fun doSigUp(@Body body: Map<String, String>): Response<SignUpResponse>

}