package com.example.bankapp

import android.app.Application
import com.example.bankapp.repository.common.SetUpDb

class BankApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SetUpDb.config(this);
    }
}