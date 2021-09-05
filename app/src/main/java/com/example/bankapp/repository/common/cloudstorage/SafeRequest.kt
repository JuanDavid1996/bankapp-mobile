package com.example.bankapp.repository.common.cloudstorage

import com.example.bankapp.repository.common.models.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class SafeRequest {
    companion object {
        suspend fun <T> safeRequest(request: suspend (() -> Result<T>)): Result<T> {
            return withContext(Dispatchers.IO) {
                try {
                    return@withContext request()
                } catch (e: Exception) {
                    return@withContext Result.Error(Exception("No podemos comunicarnos con el servidor, por favor intenta más tarde"))
                }
            }
        }
    }
}