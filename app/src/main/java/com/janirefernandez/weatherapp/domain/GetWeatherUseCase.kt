package com.janirefernandez.weatherapp.domain

import com.janirefernandez.weatherapp.data.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double) =
        repository.getCurrentWeather(latitude, longitude)
}