package com.janirefernandez.weatherapp.data.network

import com.janirefernandez.weatherapp.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("/data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("appid") appKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): Response<WeatherResponse>
}