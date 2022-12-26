package com.cursosandroidant.forecastweather

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/****
 * Project: Forecast Weather
 * From: com.cursosandroidant.forecastweather
 * Created by Sergio Rodríguez on 26/12/2022 at 11:18 a. m.
 * All rights reserved 2023.
 ***/

@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule constructor(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()) :
    TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {


    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

}