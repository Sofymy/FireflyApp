package com.example.thesis.views.loggedin

import android.location.Location
import java.nio.DoubleBuffer

interface LocationCallback {
    fun onLocation(lat: Double,long: Double):Array<Double>
}
