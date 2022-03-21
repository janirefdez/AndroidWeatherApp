package com.janirefernandez.weatherapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.janirefernandez.weatherapp.domain.GetWeatherUseCase
import com.janirefernandez.weatherapp.kits.MockWeatherUtil
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @RelaxedMockK
    private lateinit var getWeatherUseCase: GetWeatherUseCase

    private lateinit var weatherViewModel: WeatherViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        weatherViewModel = WeatherViewModel(getWeatherUseCase)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when viewModel is created, weatherViewModel should have data`() =
        runTest {
            //Given
            val weatherResponse = MockWeatherUtil.getMockWeatherResponse()
            coEvery { getWeatherUseCase(51.5, 35.6) } returns weatherResponse
            //When
            weatherViewModel.onCreate(51.5, 35.6)
            //Then
            assert(weatherViewModel.weatherResponse.value == weatherResponse)
        }

    @Test
    fun `when viewModel created and getWeatherUseCase returns null, weatherResponse should be null`() =
        runTest {
            //Given
            coEvery { getWeatherUseCase(51.5, 35.6) } returns null
            //When
            weatherViewModel.onCreate(51.5, 35.6)
            //Then
            assert(weatherViewModel.weatherResponse.value == null)
        }

    @Test
    fun `when viewModel created for second time and getWeatherUseCase returns null, weatherResponse should keep last value`() =
        runTest {
            //Given
            val weatherResponse = MockWeatherUtil.getMockWeatherResponse()
            weatherViewModel.weatherResponse.value = weatherResponse
            coEvery { getWeatherUseCase(51.5, 35.6) } returns null
            //When
            weatherViewModel.onCreate(51.5, 35.6)
            //Then
            assert(weatherViewModel.weatherResponse.value == weatherResponse)
        }

}