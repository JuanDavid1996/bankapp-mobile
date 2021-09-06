package com.example.bankapp.presentation.bank.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bankapp.R
import com.example.bankapp.contants.ViewRouterParams
import com.example.bankapp.presentation.bank.EconomicMovementsActivity
import com.example.bankapp.presentation.bank.adapters.AccountAdapter
import com.example.bankapp.presentation.bank.viewModels.AccountsViewModel
import com.example.bankapp.repository.bank.models.Account

class AccountsFragment : Fragment() {

    companion object {
        fun newInstance() = AccountsFragment()
    }

    private lateinit var viewModel: AccountsViewModel
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var accounts: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.accounts_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(AccountsViewModel::class.java)

        setUpViewReferences(view)
        setUpEvents()
        listenViewModelChanges()
        fetchAccounts()
    }

    override fun onResume() {
        super.onResume()
        fetchAccounts(true)
    }

    private fun setUpViewReferences(view: View) {
        refreshLayout = view.findViewById(R.id.refresh)
        accounts = view.findViewById(R.id.accounts)
        accounts.setHasFixedSize(true)
        accounts.layoutManager = LinearLayoutManager(context)
        accounts.adapter = AccountAdapter()
    }

    private fun setUpEvents() {
        refreshLayout.setOnRefreshListener {
            fetchAccounts(true)
        }
    }

    private fun listenViewModelChanges() {
        viewModel.accounts.observe(viewLifecycleOwner, {
            val adapter = AccountAdapter(it) { account -> onClickAccount(account) }
            accounts.adapter = adapter
        })

        viewModel.isLoading.observe(viewLifecycleOwner, {
            refreshLayout.isRefreshing = it
        })

        viewModel.onErrorMessage.observe(viewLifecycleOwner, {
            if (it.isNotBlank()) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchAccounts(refresh: Boolean = false) {
        viewModel.getAccounts(refresh)
    }

    private fun onClickAccount(account: Account) {
        println("Clicked ${account._id}")
        val intent = Intent(activity, EconomicMovementsActivity::class.java)
        intent.putExtra(ViewRouterParams.ACCOUNT_ID, account._id)
        startActivity(intent)
    }
}