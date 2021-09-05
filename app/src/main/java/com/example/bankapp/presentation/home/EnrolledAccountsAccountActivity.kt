package com.example.bankapp.presentation.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bankapp.R
import com.example.bankapp.contants.ViewRouterParams
import com.example.bankapp.presentation.home.adapters.EnrolledAccountAdapter
import com.example.bankapp.presentation.home.viewModels.EnrolledAccountsViewModel
import com.example.bankapp.repository.bank.models.EnrolledAccount
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.properties.Delegates

class EnrolledAccountsAccountActivity : AppCompatActivity() {

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var enrolledAccounts: RecyclerView
    private lateinit var beginTransfer: FloatingActionButton
    private var isTransfer by Delegates.notNull<Boolean>()
    private val model: EnrolledAccountsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tranfer_to_enrolled_account)
        isTransfer = intent.extras!!.getBoolean(ViewRouterParams.IS_TRANSFER)

        setUpViewReferences()
        setUpViewActions()
        listenViewModelUpdates()

        model.getEnrolledAccounts(isTransfer)
    }

    private fun setUpViewReferences() {
        refreshLayout = findViewById(R.id.refresh)
        enrolledAccounts = findViewById(R.id.enrolledAccounts)
        beginTransfer = findViewById(R.id.beginTransfer)

        enrolledAccounts.setHasFixedSize(true)
        enrolledAccounts.layoutManager = LinearLayoutManager(this)
        enrolledAccounts.adapter = EnrolledAccountAdapter()
    }

    private fun setUpViewActions() {
        refreshLayout.setOnRefreshListener { model.getEnrolledAccounts(true) }
    }

    private fun listenViewModelUpdates() {
        model.enrolledAccounts.observe(this, {
            enrolledAccounts.adapter =
                EnrolledAccountAdapter(it, isTransfer) { enrolledAccount, checked ->
                    onItemSelected(enrolledAccount, checked)
                }
        })

        model.isLoading.observe(this, {
            refreshLayout.isRefreshing = it
            beginTransfer.visibility = View.GONE
        })

        model.onErrorMessage.observe(this, {
            if (it.isNotBlank()) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun onItemSelected(enrolledAccount: EnrolledAccount, checked: Boolean) {
        beginTransfer.visibility =
            if (isTransfer && checked) View.VISIBLE else View.GONE
        beginTransfer.setOnClickListener {
            val intent = Intent(this, TransferTo::class.java)
            intent.putExtra(ViewRouterParams.ENROLLED_ACCOUNT_ID, enrolledAccount._id)
            startActivity(intent)
        }
    }
}