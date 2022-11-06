package com.example.thesis.model

import com.example.thesis.views.loggedin.ShareLocationState

data class Location(
    var latitude: Double,
    var longitude: Double,
    var state: ShareLocationState
)