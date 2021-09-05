package com.example.bankapp

import android.app.Application
import com.example.bankapp.repository.common.localstorage.SetUpDb

class BankApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SetUpDb.config(this);
    }
}