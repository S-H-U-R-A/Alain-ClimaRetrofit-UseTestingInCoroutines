package com.cursosandroidant.forecastweather.common.dataAccess

import com.cursosandroidant.forecastweather.entities.WeatherForecastEntity
import com.google.gson.Gson
import java.io.InputStreamReader

/****
 * Project: Forecast Weather
 * From: com.cursosandroidant.forecastweather.common.dataAccess
 * Created by Sergio Rodríguez on 27/12/2022 at 9:49 a. m.
 * All rights reserved 2023.
 ***/
class JSONFileLoader {

    private var jsonStr: String? = null

    fun loadJSONString(file: String): String? {
        //this.javaClass.classLoader?.getResourceAsStream(file)
        //LA LINEA ANTERIOR CARGA/LOADER UN ARCHIVO Y CON AsStream LO CONVERTIMOS a STREAM
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        return jsonStr
    }

    fun loadWeatherForecastEntity(file: String): WeatherForecastEntity? {
        val loader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(file))
        jsonStr = loader.readText()
        loader.close()
        //CONVERTIMO EL ARCHIVO A UNA ENTIDAD O MODELO QUE DEFINIMOS DE KOTLIN
        return Gson().fromJson(jsonStr, WeatherForecastEntity::class.java)
    }

}