package com.example.bankapp.presentation.bank.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.presentation.bank.viewModels.ProfileViewModel
import com.example.bankapp.presentation.session.SignInActivity
import com.google.android.material.textfield.TextInputEditText

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var model: ProfileViewModel
    private lateinit var logout: Button
    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var username: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(this).get(ProfileViewModel::class.java)
        setUpViewReferences(view)
        setUpButtonActions()
        listenViewModelUpdates()
        model.onCreate()
    }

    private fun setUpViewReferences(view: View) {
        firstName = view.findViewById(R.id.firstName)
        lastName = view.findViewById(R.id.lastName)
        username = view.findViewById(R.id.username)
        logout = view.findViewById(R.id.logout)
    }

    private fun setUpButtonActions() {
        logout.setOnClickListener {
            model.logOut();
        }
    }

    private fun listenViewModelUpdates() {
        model.onLogOut.observe(viewLifecycleOwner, {
            if (it) toSignIn()
        })

        model.user.observe(viewLifecycleOwner, {
            if (it != null) {
                firstName.setText(it.firstName)
                lastName.setText(it.lastName)
                username.setText(it.username)
            }
        })
    }

    private fun toSignIn() {
        startActivity(Intent(context, SignInActivity::class.java))
        activity?.finish()
    }

}