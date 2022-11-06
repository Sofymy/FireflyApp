package com.example.thesis.model

import java.text.SimpleDateFormat
import java.util.*

data class Weather(
    val updatedAtText : String,
    val temp : String,
    val tempMin : String,
    val tempMax : String,
    val pressure : String,
    val humidity : String,
    val sunrise : Long,
    val sunset : Long,
    val windSpeed : String,
    val weatherDescription : String,
    val address : String
)
