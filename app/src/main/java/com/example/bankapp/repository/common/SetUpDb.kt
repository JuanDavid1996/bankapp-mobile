package com.example.bankapp.repository.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bankapp.repository.bank.localstorage.AccountDao
import com.example.bankapp.repository.bank.localstorage.EnrolledAccountDao
import com.example.bankapp.repository.bank.localstorage.MovementDao
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.repository.bank.models.EnrolledAccount
import com.example.bankapp.repository.bank.models.Movement
import com.example.bankapp.repository.session.localstorage.SessionDao
import com.example.bankapp.repository.session.localstorage.UserDao
import com.example.bankapp.repository.session.models.Session
import com.example.bankapp.repository.session.models.User

@Database(
    entities = [User::class, Session::class, Account::class, EnrolledAccount::class, Movement::class],
    version = 5
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun accountDao(): AccountDao
    abstract fun enrolledAccountDao(): EnrolledAccountDao
    abstract fun movementDao(): MovementDao
}

class SetUpDb {
    companion object {
        lateinit var db: AppDatabase

        fun config(context: Context) {
            db = Room.databaseBuilder(context, AppDatabase::class.java, "bankapp")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}