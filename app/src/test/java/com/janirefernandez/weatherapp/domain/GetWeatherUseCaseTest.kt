package com.janirefernandez.weatherapp.domain

import com.janirefernandez.weatherapp.data.WeatherRepository
import com.janirefernandez.weatherapp.kits.MockWeatherUtil
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class GetWeatherUseCaseTest {

    @RelaxedMockK
    private lateinit var weatherRepository: WeatherRepository

    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getWeatherUseCase = GetWeatherUseCase(weatherRepository)
    }

    @Test
    fun `when the api return something then get values from api`() = runBlocking {
        //Given
        val weatherResponse = MockWeatherUtil.getMockWeatherResponse()
        coEvery {
            weatherRepository.getCurrentWeather(51.5, 35.6)
        } returns weatherResponse
        //When
        val response = getWeatherUseCase(51.5, 35.6)
        //Then
        coVerify(exactly = 1) { weatherRepository.getCurrentWeather(51.5, 35.6) }
        assert(response == weatherResponse)
    }
}