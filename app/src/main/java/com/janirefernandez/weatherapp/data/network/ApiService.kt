package com.janirefernandez.weatherapp.data.network

import com.janirefernandez.weatherapp.data.model.WeatherResponse
import com.janirefernandez.weatherapp.di.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApiService @Inject constructor(private val api: ApiClient) {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): WeatherResponse? {
        return withContext(Dispatchers.IO) {
            val response = api.getCurrentWeather(Constants.apiKey, latitude, longitude)
            response.body()
        }
    }
}