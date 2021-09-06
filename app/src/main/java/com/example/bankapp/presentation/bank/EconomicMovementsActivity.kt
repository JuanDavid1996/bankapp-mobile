package com.example.bankapp.presentation.bank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bankapp.R
import com.example.bankapp.contants.ModelConstants.Companion.CURRENT_ES
import com.example.bankapp.contants.ModelConstants.Companion.SAVING
import com.example.bankapp.contants.ModelConstants.Companion.SAVING_ES
import com.example.bankapp.contants.ViewRouterParams
import com.example.bankapp.presentation.bank.adapters.MovementAdapter
import com.example.bankapp.presentation.bank.viewModels.EconomicMovementsViewModel

class EconomicMovementsActivity : AppCompatActivity() {

    val model: EconomicMovementsViewModel by viewModels()
    private lateinit var accountType: TextView
    private lateinit var accountNumber: TextView
    private lateinit var movements: RecyclerView
    private lateinit var refreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_economic_movements)
        setUpViewReferences()
        setUpViewActions()
        listenViewModelUpdates()
        model.onCreate(intent.extras!!.getString(ViewRouterParams.ACCOUNT_ID)!!)
    }

    private fun setUpViewReferences() {
        refreshLayout = findViewById(R.id.refresh)
        accountType = findViewById(R.id.accountType)
        accountNumber = findViewById(R.id.accountNumber)
        movements = findViewById(R.id.movements)
        movements.setHasFixedSize(true)
        movements.layoutManager = LinearLayoutManager(this)
        movements.adapter = MovementAdapter()
    }

    private fun setUpViewActions() {
        refreshLayout.setOnRefreshListener { model.onRefresh() }
    }

    private fun listenViewModelUpdates() {
        model.account.observe(this, {
            if (it != null) {
                accountType.text = if (it.type == SAVING) SAVING_ES else CURRENT_ES
                accountNumber.text = it.number
            }
        })

        model.isLoading.observe(this, {
            refreshLayout.isRefreshing = it
        })

        model.onErrorMessage.observe(this, {
            if (it.isNotEmpty()) Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        model.movements.observe(this, {
            println("RESULT COUNT ${it.size}")
            movements.adapter = MovementAdapter(it)
        })
    }
}