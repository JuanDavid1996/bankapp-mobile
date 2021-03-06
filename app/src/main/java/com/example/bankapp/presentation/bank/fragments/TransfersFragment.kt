package com.example.bankapp.presentation.bank.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.contants.ViewRouterParams
import com.example.bankapp.presentation.bank.*
import com.example.bankapp.presentation.bank.viewModels.TransfersViewModel

class TransfersFragment : Fragment() {

    companion object {
        fun newInstance() = TransfersFragment()
    }

    private lateinit var viewModel: TransfersViewModel
    private lateinit var registeredAccount: TextView
    private lateinit var noRegisteredAccount: TextView
    private lateinit var registerAccount: TextView
    private lateinit var enrolledAccounts: TextView
    private lateinit var receive: TextView
    private lateinit var send: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.transfers_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(TransfersViewModel::class.java)
        setUpViewReference(view)
        setUpViewActions()
    }

    private fun setUpViewReference(view: View) {
        registeredAccount = view.findViewById(R.id.registeredAccount)
        noRegisteredAccount = view.findViewById(R.id.noRegisteredAccount)
        registerAccount = view.findViewById(R.id.registerAccount)
        enrolledAccounts = view.findViewById(R.id.enrolledAccounts)
        receive = view.findViewById(R.id.receive)
        send = view.findViewById(R.id.send)
    }

    private fun setUpViewActions() {
        registeredAccount.setOnClickListener { toEnrolledAccounts(true) }
        noRegisteredAccount.setOnClickListener { toTransferToNotEnrolledAccount() }
        registerAccount.setOnClickListener { toEnrollAccount() }
        enrolledAccounts.setOnClickListener { toEnrolledAccounts() }
        receive.setOnClickListener { toGenerateQR() }
        send.setOnClickListener { toSendWithQR() }
    }

    private fun toEnrollAccount() {
        startActivity(Intent(activity, EnrollAccountActivity::class.java))
    }

    private fun toEnrolledAccounts(isTransfer: Boolean = false) {
        val intent = Intent(activity, EnrolledAccountsAccountActivity::class.java)
        intent.putExtra(ViewRouterParams.IS_TRANSFER, isTransfer)
        startActivity(intent)
    }

    private fun toTransferToNotEnrolledAccount() {
        val intent = Intent(activity, TransferToNoEnrolledAccount::class.java)
        startActivity(intent)
    }

    private fun toGenerateQR() {
        startActivity(Intent(activity, GenerateTransferByQR::class.java))
    }

    private fun toSendWithQR() {
        startActivity(Intent(activity, TransferWithQR::class.java))
    }
}