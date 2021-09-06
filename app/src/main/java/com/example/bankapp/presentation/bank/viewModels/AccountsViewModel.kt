package com.example.bankapp.presentation.bank.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bankapp.repository.bank.AccountRepository
import com.example.bankapp.repository.bank.models.Account
import com.example.bankapp.network.models.Result
import com.example.bankapp.repository.weather.WeatherRepository
import com.example.bankapp.repository.weather.models.Weather
import kotlinx.coroutines.launch
import org.json.JSONObject

class AccountsViewModel : ViewModel() {
    private val accountRepository = AccountRepository()
    private val weatherRepository = WeatherRepository()

    val accounts: MutableLiveData<List<Account>> by lazy {
        MutableLiveData<List<Account>>(emptyList())
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val onErrorMessage: MutableLiveData<String> by lazy {
        MutableLiveData("")
    }

    val weatherStatus: MutableLiveData<String> by lazy {
        MutableLiveData("Cargando ...")
    }

    fun getAccounts(refresh: Boolean) {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (val result = accountRepository.getAccounts(refresh)) {
                is Result.Success -> {
                    accounts.postValue(result.data.data)
                }
                is Result.Error -> {
                    onErrorMessage.postValue(result.exception.message)
                }
            }
            isLoading.postValue(false)
        }
    }

    fun getWeatherStatus(lat: Double, lng: Double) {
        viewModelScope.launch {
            when (val result = weatherRepository.getWeather(lat, lng)) {
                is Result.Success -> {
                    val weather = result.data
                    val status =
                        "${weather.location.country}/${weather.location.region}/${weather.location.name}" +
                                "\nTemp. ${weather.current.tempC} - Condiciones: ${weather.current.condition.text}"
                    weatherStatus.postValue(status)
                }
                is Result.Error -> {
                    onErrorMessage.postValue(result.exception.message)
                }
                else -> {
                    weatherStatus.postValue("Algo estraño sucedio, no se pudo obtener estado del tiempo")
                }
            }
        }
    }
}