package com.example.bankapp.repository.session.cloudstorage

import com.example.bankapp.repository.common.AppProvider
import com.example.bankapp.repository.session.models.SignInResponse
import com.example.bankapp.repository.session.models.SignUpResponse
import retrofit2.Response

class SessionProvider : AppProvider() {
    suspend fun doSignIn(username: String, password: String): Response<SignInResponse> {
        val data: MutableMap<String, String> = HashMap()
        data["username"] = username;
        data["password"] = password;
        return getRetrofit().create(SessionService::class.java).doSigIn(data)
    }

    suspend fun doSignUp(
        firstName: String,
        lastName: String,
        identification: String,
        username: String,
        password: String
    ): Response<SignUpResponse> {
        val data: MutableMap<String, String> = HashMap()
        data["firstName"] = firstName;
        data["lastName"] = lastName;
        data["identification"] = identification;
        data["username"] = username;
        data["password"] = password;
        return getRetrofit().create(SessionService::class.java).doSigUp(data)
    }
}