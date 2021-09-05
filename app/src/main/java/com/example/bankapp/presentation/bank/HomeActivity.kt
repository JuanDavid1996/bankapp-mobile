package com.example.bankapp.presentation.bank

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bankapp.R
import com.example.bankapp.presentation.bank.fragments.AccountsFragment
import com.example.bankapp.presentation.bank.fragments.ProfileFragment
import com.example.bankapp.presentation.bank.fragments.TransfersFragment
import com.example.bankapp.presentation.bank.viewModels.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var tabs: BottomNavigationView

    private val model: HomeViewModel by viewModels()

    private val accountsFragment = AccountsFragment.newInstance()
    private val transfersFragment = TransfersFragment.newInstance()
    private val profileFragment = ProfileFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUpViewReferences()
        setUpBottomNavigation()
        showTab(R.id.accounts)
    }

    private fun setUpViewReferences() {
        tabs = findViewById(R.id.tabs)
    }

    private fun setUpBottomNavigation() {
        tabs.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.accounts -> {
                    showTab(it.itemId)
                    true
                }
                R.id.transfers -> {
                    showTab(it.itemId)
                    true
                }
                R.id.profile -> {
                    showTab(it.itemId)
                    true
                }
                else -> false
            }
        }
    }

    private fun showTab(tabId: Int) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        when (tabId) {
            R.id.accounts -> {
                transaction.replace(R.id.container, accountsFragment).commit();
            }
            R.id.transfers -> {
                transaction.replace(R.id.container, transfersFragment).commit();
            }
            R.id.profile -> {
                transaction.replace(R.id.container, profileFragment).commit();
            }
        }
    }

}