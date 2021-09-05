package com.example.bankapp.presentation.home.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.bankapp.R
import com.example.bankapp.presentation.home.EnrollAccountActivity
import com.example.bankapp.presentation.home.viewModels.TransfersViewModel

class TransfersFragment : Fragment() {

    companion object {
        fun newInstance() = TransfersFragment()
    }

    private lateinit var viewModel: TransfersViewModel
    private lateinit var registeredAccount: TextView
    private lateinit var noRegisteredAccount: TextView
    private lateinit var registerAccount: TextView

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
    }

    private fun setUpViewActions() {
        registeredAccount.setOnClickListener {}
        noRegisteredAccount.setOnClickListener {}
        registerAccount.setOnClickListener { toRegisterAccount() }
    }

    private fun toRegisterAccount() {
        startActivity(Intent(activity, EnrollAccountActivity::class.java))
    }

}