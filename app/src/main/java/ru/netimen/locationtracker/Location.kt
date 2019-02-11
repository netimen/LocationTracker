package ru.netimen.locationtracker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Singleton

class Location(val lat: Float, val lng: Float, val timeMs: Long) {
    constructor(location: android.location.Location) : this(
        location.latitude.toFloat(),
        location.longitude.toFloat(),
        location.time
    )

    companion object {
        @SuppressLint("SimpleDateFormat")
        private val DATE_FORMATTER = SimpleDateFormat("dd.MM HH:mm:ss")
    }

    override fun toString() = "${"%.3f".format(lat)} ${"%.3f".format(lng)} ${DATE_FORMATTER.format(timeMs)}"
}

@Singleton
class LocationManager @Inject constructor(context: Context) {
    private val locationApi by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private var locationCallback: LocationCallback? = null

    fun startListen(intervalMs: Int, listener: (Location) -> Unit) {
        locationCallback?.let { locationApi.removeLocationUpdates(locationCallback) }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) = listener(Location(result.lastLocation))
        }

        locationApi.requestLocationUpdates(createLocationRequest(intervalMs), locationCallback, Looper.getMainLooper())
    }

    private fun createLocationRequest(intervalMs: Int) =
        LocationRequest.create().setFastestInterval(intervalMs.toLong()).setInterval(intervalMs.toLong())
}
