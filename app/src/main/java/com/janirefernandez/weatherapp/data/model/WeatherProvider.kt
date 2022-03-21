package com.janirefernandez.weatherapp.data.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherProvider @Inject constructor() {
    var weatherResponse: WeatherResponse? = null
}