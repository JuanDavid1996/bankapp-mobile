package com.example.bankapp.repository.bank

import com.example.bankapp.network.utils.SafeRequest
import com.example.bankapp.repository.bank.cloudstorage.MovementProvider
import com.example.bankapp.repository.bank.models.Movement
import com.example.bankapp.network.models.Result
import com.example.bankapp.network.utils.NormalizeError
import com.example.bankapp.repository.common.SetUpDb.Companion.db
import com.example.bankapp.repository.session.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class MovementRepository {
    private val provider = MovementProvider()
    private val sessionRepository = SessionRepository()

    suspend fun getMovements(accountId: String, forceRefresh: Boolean): Result<List<Movement>> {
        return SafeRequest.safeRequest {
            var movements = db.movementDao().getMovementsByAccountId(accountId)
            if (movements.isEmpty() || forceRefresh) {
                val result = getMovementFromCloud(accountId)
                if (result is Result.Success) {
                    movements = result.data
                    forceSaveMovements(accountId, movements)
                } else {
                    return@safeRequest result
                }
            }
            return@safeRequest Result.Success(movements)
        }
    }

    private suspend fun getMovementFromCloud(accountId: String): Result<List<Movement>> {
        return SafeRequest.safeRequest {
            val session = sessionRepository.getSession()
            val call = provider.getMovements(session = session, accountId)
            if (call.isSuccessful) {
                val response = call.body()
                if (response!!.success) {
                    return@safeRequest Result.Success(response.data!!)
                } else {
                    return@safeRequest NormalizeError.safeError(response.errors)
                }
            }
            return@safeRequest Result.Error(Exception("Request fail"))
        }
    }

    private suspend fun forceSaveMovements(accountId: String, movements: List<Movement>) {
        withContext(Dispatchers.IO) {
            removeMovementWithAccountId(accountId)
            insertMovements(movements)
        }
    }

    private suspend fun removeMovementWithAccountId(accountId: String) {
        withContext(Dispatchers.IO) {
            val movements = db.movementDao().getMovementsByAccountId(accountId)
            if (movements.isNotEmpty()) movements.forEach { db.movementDao().delete(it) }
        }
    }

    private suspend fun insertMovements(movements: List<Movement>) {
        withContext(Dispatchers.IO) {
            if (movements.isNotEmpty()) movements.forEach { db.movementDao().insert(it) }
        }
    }
}