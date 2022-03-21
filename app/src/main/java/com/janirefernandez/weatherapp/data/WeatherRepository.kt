package com.janirefernandez.weatherapp.data

import com.janirefernandez.weatherapp.data.model.WeatherProvider
import com.janirefernandez.weatherapp.data.model.WeatherResponse
import com.janirefernandez.weatherapp.data.network.ApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService,
    private val weatherProvider: WeatherProvider
) {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherResponse? {
        val response = apiService.getCurrentWeather(latitude, longitude)
        weatherProvider.weatherResponse = response
        return response
    }
}