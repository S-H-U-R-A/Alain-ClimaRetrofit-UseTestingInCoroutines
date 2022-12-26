package com.cursosandroidant.forecastweather.mainModule.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.cursosandroidant.forecastweather.MainCoroutineRule
import com.cursosandroidant.forecastweather.common.dataAccess.WeatherForecastService
import com.cursosandroidant.forecastweather.entities.WeatherForecastEntity
import com.cursosandroidant.historicalweatherref.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/****
 * Project: Forecast Weather
 * From: com.cursosandroidant.forecastweather.mainModule.viewModel
 * Created by Sergio Rodríguez on 26/12/2022 at 9:23 a. m.
 * All rights reserved 2023.
 */

class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutinesRule = MainCoroutineRule()

    //VARIABLE VIEW MODEL
    private lateinit var mainViewModel: MainViewModel
    //VARIABLE DEL SERVICIO
    private lateinit var service: WeatherForecastService

    companion object {
        //PARA CONFIGURAR TODA LA CLASE CON RETROFIT USAMOS @BeforeClass
        private lateinit var retrofit: Retrofit

        @BeforeClass
        @JvmStatic
        fun setupCommon() {
            retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }

    //INICIALIZAMOS LAS VARIABLES
    @Before
    fun setUp() {
        mainViewModel = MainViewModel()
        service = retrofit.create(WeatherForecastService::class.java)
    }

    //VALIDAMOS QUE EL SERVICIO NO NOS RETORNE UN NULL
    @Test
    fun checkCurrentWeather_isNotNUllTest() = runBlocking {
        val result = service.getWeatherForecastByCoordinates(
            19.4342, -99.1962, "d09b4c22cd9bac96aa35b48f0c5dbbc3",
            "metric", "en"
        )

        assertThat(result.current, `is`(notNullValue()))
    }

    //VALIDAMOS QUE NOS RETORNE LA ZONA HORARIA INDICADA
    @Test
    fun checkTimeZone_returnMexicoCityTest() = runBlocking {
        val result = service.getWeatherForecastByCoordinates(
            19.4342, -99.1962, "d09b4c22cd9bac96aa35b48f0c5dbbc3",
            "metric", "en"
        )

        assertThat(result.timezone, `is`("America/Mexico_City"))
    }

    //VALIDAMOS EL MENSAJE DE ERROR
    @Test
    fun checkErrorResponseWithOnlyCoordinatesTest(): Unit = runBlocking {

        try {

            service.getWeatherForecastByCoordinates(
                19.4342, -99.1962, "",
                "", ""
            )

        } catch (e: Exception) {
            assertThat(e.localizedMessage, `is`("HTTP 401 Unauthorized"))
        }
    }


    @Test
    fun checkHourly_sizeTest() = runBlocking {


        //EL CODIGO COMENTADO ES SIN USAR EL ARCHIVO DE TEST DE GOOGLE

        /* val observer = Observer<WeatherForecastEntity>{}

       try {
            mainViewModel.getResult().observeForever(observer)
            mainViewModel.getWeatherAndForecast(19.4342, -99.1962, "d09b4c22cd9bac96aa35b48f0c5dbbc3",
                "metric", "en")
            val result = mainViewModel.getResult().value
            assertThat(result?.hourly?.size, `is`(48))
        } finally {
            mainViewModel.getResult().removeObserver(observer)
        }*/

        mainViewModel.getWeatherAndForecast(19.4342, -99.1962, "d09b4c22cd9bac96aa35b48f0c5dbbc3",
            "metric", "en")
        val result = mainViewModel.getResult().getOrAwaitValue()
        assertThat(result?.hourly?.size, `is`(48))

    }

}