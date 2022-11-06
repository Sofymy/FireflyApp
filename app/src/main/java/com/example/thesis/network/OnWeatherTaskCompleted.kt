package com.example.thesis.network

import com.example.thesis.model.Weather

interface OnWeatherTaskCompleted {
    fun onTaskCompleted(weather: Weather)
}