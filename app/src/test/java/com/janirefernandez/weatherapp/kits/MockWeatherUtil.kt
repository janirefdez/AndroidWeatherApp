package com.janirefernandez.weatherapp.kits

import com.google.gson.Gson
import com.janirefernandez.weatherapp.data.model.WeatherResponse
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

class MockWeatherUtil {

    companion object {
        private const val ASSETS_FILE = "assets/weatherResponse.json"
        fun getMockWeatherResponse(): WeatherResponse? {
            val inputStream =
                MockWeatherUtil::class.java.classLoader!!.getResourceAsStream(ASSETS_FILE)
            val result: String = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
            return Gson().fromJson(result, WeatherResponse::class.java)
        }
    }

}