package com.janirefernandez.weatherapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.janirefernandez.weatherapp.data.model.WeatherResponse
import com.janirefernandez.weatherapp.domain.GetWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    val weatherResponse = MutableLiveData<WeatherResponse>()

    fun onCreate(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val result = getWeatherUseCase(latitude, longitude)
            if (result != null) {
               weatherResponse.postValue(result)
            }
        }
    }

}