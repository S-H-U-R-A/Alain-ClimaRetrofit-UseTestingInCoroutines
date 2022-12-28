package com.cursosandroidant.forecastweather.common.dataAccess

import com.cursosandroidant.forecastweather.entities.WeatherForecastEntity
import com.google.gson.Gson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import javax.net.ssl.HttpsURLConnection

/****
 * Project: Forecast Weather
 * From: com.cursosandroidant.forecastweather.common.dataAccess
 * Created by Sergio Rodríguez on 27/12/2022 at 10:03 a. m.
 * All rights reserved 2023.
 ***/

@RunWith(MockitoJUnitRunner::class)
class ResponseServerTest {

    //CREAMOS UNA VARIABLE DE TIPO MOCK WEBSERVER
    private lateinit var mockWebServer: MockWebServer

    //PARA CADA TEST INICIAMOS EL SERVIDOR Y LO CERRAMOS AL FINALIZAR EL TEST
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    //En esta prueba estamos validando que se pueda leer de los archivos JSON creado
    @Test
    fun `read json file success`() {

        val reader = JSONFileLoader().loadJSONString("weather_forecast_response_success")

        assertThat(reader, `is`(notNullValue()))

        assertThat(reader, containsString("America/Mexico_City"))

    }

    //EN ESTA PRUEBA SE ESTA VA A VALIDAR QUE EXISTA UN TIMEZONE EN LA RESPUESTA FAKE
    @Test
    fun `get weatherForecast and check timeZone exist`() {
        //SE CREA LA RESPUESTA QUE QUEREMOS DEVOLVER FAKE
        val response =
            MockResponse()
                //SE SETEA EL CODIGO DE RESPUESTA QUE QUEREMOS
                .setResponseCode(HttpsURLConnection.HTTP_OK)
                //SE SETEA EL CUERPO QUE QUEREMOS EN LA RESPUESTA
                .setBody(
                    JSONFileLoader().loadJSONString("weather_forecast_response_success")
                        ?: "{errorCode: 34}"
                )

        //SE AÑADE LA RESPUESTA A LA COLA DE RESPUESTAS ENVIADAS DESDE EL FAKE SERVER
        mockWebServer.enqueue(response)

        //SE VALIDA QUE LA RESPUESTA CONTENGA EL STRING timezone
        assertThat(response.getBody()?.readUtf8(), containsString("\"timezone\""))
    }

    //SE PRUEBA EL OTRO ARCHIVO DE RESPUESTA ERRADA
    @Test
    fun `get weatherForecast and check fail responseTest`() {
        //SE CREA LA RESPUESTA QUE QUEREMOS DEVOLVER FAKE
        val response =
            MockResponse()
                //SE SETEA EL CODIGO DE RESPUESTA QUE QUEREMOS
                .setResponseCode(HttpsURLConnection.HTTP_OK)
                //SE SETEA EL CUERPO QUE QUEREMOS EN LA RESPUESTA
                .setBody(
                    JSONFileLoader().loadJSONString("weather_forecast_response_fail")
                        ?: "{errorCode: 34}"
                )
        //SE AÑADE LA RESPUESTA A LA COLA DE RESPUESTAS ENVIADAS DESDE EL FAKE SERVER
        mockWebServer.enqueue(response)

        //SE VALIDA QUE LA RESPUESTA CONTENGA EL STRING
        assertThat(
            response.getBody()?.readUtf8(),
            containsString(
                "{\"cod\": 401, \"message\": \"Invalid API key. Please see https://openweathermap.org/faq#error401 for more info.\"}"
            )
        )
    }


    @Test
    fun `get weatherForecast and check contains hourlyTest list no empty`(){
        //SE CREA LA RESPUESTA QUE QUEREMOS DEVOLVER FAKE
        val response =
            MockResponse()
                //SE SETEA EL CODIGO DE RESPUESTA QUE QUEREMOS
                .setResponseCode(HttpsURLConnection.HTTP_OK)
                //SE SETEA EL CUERPO QUE QUEREMOS EN LA RESPUESTA
                .setBody(
                    JSONFileLoader().loadJSONString("weather_forecast_response_success")
                        ?: "{errorCode: 34}"
                )
        //SE AÑADE LA RESPUESTA A LA COLA DE RESPUESTAS ENVIADAS DESDE EL FAKE SERVER
        mockWebServer.enqueue(response)

        //SE VALIDA QUE hourly ESTE EN LA RESPUESTA
        assertThat(response.getBody()?.readUtf8(), containsString("\"hourly\""))

        //OBTENERMOS LA RESPUESTA EN UN TIPO DE KOTLIN
        val jsonIntoKotlin = Gson().fromJson(response.getBody()?.readUtf8() ?: "", WeatherForecastEntity::class.java)

        //VALIDAMOS QUE hourly NO ESTE VACIO, PERO YA TRABAJANDO LA DATA COMO UNA ENTIDAD DE KOTLIN
        assertThat( jsonIntoKotlin.hourly.isEmpty(), `is`(false))
    }




}