package com.example.bankapp.presentation.bank.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bankapp.R
import com.example.bankapp.contants.ViewRouterParams
import com.example.bankapp.presentation.bank.EconomicMovementsActivity
import com.example.bankapp.presentation.bank.adapters.AccountAdapter
import com.example.bankapp.presentation.bank.helper.PermissionChecker
import com.example.bankapp.presentation.bank.viewModels.AccountsViewModel
import com.example.bankapp.repository.bank.models.Account
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.provider.TedPermissionProvider


class AccountsFragment : Fragment() {

    companion object {
        fun newInstance() = AccountsFragment()
    }

    private lateinit var viewModel: AccountsViewModel
    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var accounts: RecyclerView
    private lateinit var weatherStatus: TextView
    private lateinit var checkPermission: Button
    private var fusedLocationClient: FusedLocationProviderClient? = null


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
        checkAppPermissions()
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
        weatherStatus = view.findViewById(R.id.weatherStatus)
        checkPermission = view.findViewById(R.id.checkPermission)
    }

    private fun setUpEvents() {
        refreshLayout.setOnRefreshListener {
            fetchAccounts(true)
            getLastLocation()
        }
        checkPermission.setOnClickListener { checkAppPermissions() }
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

        viewModel.weatherStatus.observe(viewLifecycleOwner, {
            if (it != null) {
                weatherStatus.text = it
            }
        })
    }

    private fun fetchAccounts(refresh: Boolean = false) {
        viewModel.getAccounts(refresh)
    }

    private fun onClickAccount(account: Account) {
        val intent = Intent(activity, EconomicMovementsActivity::class.java)
        intent.putExtra(ViewRouterParams.ACCOUNT_ID, account._id)
        startActivity(intent)
    }

    private fun checkAppPermissions() {
        PermissionChecker.checkAppPermissions(object : PermissionListener {
            override fun onPermissionGranted() {
                setUpGps()
                checkPermission.visibility = View.GONE
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                weatherStatus.text = "No ha concedido todo los permisos"
                checkPermission.visibility = View.VISIBLE
            }
        })
    }

    private fun setUpGps() {
        val enabled = isGPSEnabled()
        if (enabled) {
            getLastLocation()
        } else {
            showGPSEnableDialog()
        }
    }

    private fun isGPSEnabled(): Boolean {
        val lm =
            TedPermissionProvider.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var gpsEnabled = false
        var networkEnabled = false

        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        return gpsEnabled && networkEnabled;
    }

    private fun showGPSEnableDialog() {
        AlertDialog.Builder(context)
            .setMessage("Gps deshabilitado")
            .setPositiveButton(
                "Habilitar"
            ) { _, _ ->
                context?.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private fun getLastLocation() {
        if (!isGPSEnabled()) return

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient!!.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    viewModel.getWeatherStatus(location.latitude, location.longitude)
                }
            }
    }
}