package com.example.bankapp.presentation.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.R
import com.example.bankapp.presentation.home.viewModels.ProfileViewModel
import com.example.bankapp.presentation.session.SignInActivity

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var logout: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        setUpViewReferences(view)
        setUpButtonActions()
        listenViewModelUpdates();
    }

    private fun setUpViewReferences(view: View) {
        logout = view.findViewById(R.id.logout)
    }

    private fun setUpButtonActions() {
        logout.setOnClickListener {
            viewModel.logOut();
        }
    }

    private fun listenViewModelUpdates() {
        viewModel.onLogOut.observe(viewLifecycleOwner, {
            if (it) toSignIn()
        })
    }

    private fun toSignIn() {
        startActivity(Intent(context, SignInActivity::class.java))
        activity?.finish()
    }

}