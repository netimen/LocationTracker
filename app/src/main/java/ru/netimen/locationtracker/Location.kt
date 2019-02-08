package ru.netimen.locationtracker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat

class Location(val lat: Float, val lng: Float, val timeMs: Long) {
    companion object {
        @SuppressLint("SimpleDateFormat")
        private val DATE_FORMATTER = SimpleDateFormat("hh:mm:ss")
    }

    override fun toString() = "${"%.3f".format(lat)} ${"%.3f".format(lng)} ${DATE_FORMATTER.format(timeMs)}"
}

class LocationManager(context: Context) {
    private val locationApi by lazy {
        LocationServices.getFusedLocationProviderClient(
            context
        )
    }

    fun startListen(intervalMs: Int, listener: (Location) -> Unit) {
        locationApi.requestLocationUpdates( // CUR cache last location
            LocationRequest.create().setFastestInterval(intervalMs.toLong()).setInterval(intervalMs.toLong()),
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    listener(
                        Location(
                            result.lastLocation.latitude.toFloat(),
                            result.lastLocation.longitude.toFloat(),
                            result.lastLocation.time
                        )
                    )
                }
            },
            Looper.getMainLooper()
        )
    }
}
