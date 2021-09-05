package com.example.bankapp.network.utils

import com.example.bankapp.network.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class SafeRequest {
    companion object {
        suspend fun <T> safeRequest(cb: suspend (() -> Result<T>)): Result<T> {
            return withContext(Dispatchers.IO) {
                try {
                    return@withContext cb()
                } catch (e: Exception) {
                    return@withContext Result.Error(Exception("No podemos comunicarnos con el servidor, por favor intenta más tarde"))
                }
            }
        }
    }
}