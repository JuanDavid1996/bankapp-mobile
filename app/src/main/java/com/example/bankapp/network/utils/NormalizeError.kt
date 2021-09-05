package com.example.bankapp.network.utils

import com.example.bankapp.network.models.Result

import java.lang.Exception

class NormalizeError {
    companion object {
        fun <T> safeError(errors: List<String>, onlyFirst: Boolean = true): Result<T> {
            if (errors.isEmpty()) {
                return Result.Error(Exception("Fallo la solicitud"))
            }
            val message = if (onlyFirst) errors[0] else errors.joinToString { ".\n" }
            return Result.Error(Exception(message))
        }
    }
}