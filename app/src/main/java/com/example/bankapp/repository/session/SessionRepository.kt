package com.example.bankapp.repository.session

import com.example.bankapp.repository.common.localstorage.SetUpDb.Companion.db
import com.example.bankapp.repository.session.models.SignInResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import com.example.bankapp.repository.common.models.Result
import com.example.bankapp.repository.session.cloudstorage.SessionProvider
import com.example.bankapp.repository.session.models.Session
import com.example.bankapp.repository.session.models.SignUpResponse
import com.example.bankapp.repository.session.models.User

class SessionRepository {
    var provider = SessionProvider()

    suspend fun getSession(): Session {
        return withContext(Dispatchers.IO) {
            if (!hasSession()) throw Exception("No ha iniciado session")
            return@withContext db.sessionDao().getCurrentSession()!!
        }
    }

    suspend fun hasSession(): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext db.sessionDao().getCurrentSession() != null
        }
    }

    suspend fun logOut() {
        return withContext(Dispatchers.IO) {
            db.clearAllTables();
            return@withContext
        }
    }

    suspend fun doSignIn(username: String, password: String): Result<SignInResponse?> {
        return withContext(Dispatchers.IO) {
            val call = provider.doSignIn(username, password)
            val response = call.body()
            if (call.isSuccessful) {
                if (response?.success!!) {
                    val user = response.data!!.user

                    db.sessionDao().insert(
                        Session(
                            token = response.data!!.token!!,
                            userId = user._id
                        )
                    )

                    db.userDao().insert(
                        User(
                            _id = user._id,
                            identification = user.identification,
                            username = user.username,
                            firstName = user.firstName,
                            lastName = user.lastName,
                        )
                    )

                    return@withContext Result.Success(response)
                } else {
                    return@withContext Result.Error(Exception(if (response.errors.isNotEmpty()) response.errors[0] else "Request fail"))
                }
            }
            return@withContext Result.Error(Exception("Request fail"))
        }
    }

    suspend fun doSignUp(
        firstName: String,
        lastName: String,
        identification: String,
        username: String,
        password: String
    ): Result<SignUpResponse?> {
        return withContext(Dispatchers.IO) {
            val call = provider.doSignUp(firstName, lastName, identification, username, password)
            val response = call.body()
            if (call.isSuccessful) {
                println("OK SUCCESS 200")
                if (response?.success!!) {
                    println("OK PASO POR AQUÍ?")
                    return@withContext Result.Success(response)
                } else {
                    return@withContext Result.Error(Exception(if (response.errors.isNotEmpty()) response.errors[0] else "Request fail"))
                }
            }
            return@withContext Result.Error(Exception("Request fail"))
        }
    }
}