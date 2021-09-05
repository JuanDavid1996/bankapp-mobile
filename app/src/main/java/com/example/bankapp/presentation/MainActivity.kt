package com.example.bankapp.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import com.example.bankapp.presentation.home.HomeActivity
import com.example.bankapp.presentation.session.SignInActivity
import com.example.bankapp.repository.session.SessionRepository
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            val hasSession = SessionRepository().hasSession()
            runOnUiThread {
                val intent: Intent = if (hasSession) {
                    Intent(this@MainActivity, HomeActivity::class.java)
                } else {
                    Intent(this@MainActivity, SignInActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}