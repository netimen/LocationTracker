package ru.netimen.locationtracker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val locationView by lazy { findViewById<TextView>(R.id.location) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UUID.randomUUID()
        locationView.text = Location(65.343, 12.1234, System.currentTimeMillis()).toString()
        LocationManager(this.applicationContext).startListen(10L) {
            locationView.text = it.toString()
        }
    }
}

class Location(val lat: Double, val lng: Double, val time: Long) {
    companion object {
        @SuppressLint("SimpleDateFormat")
        private val DATE_FORMATTER = SimpleDateFormat("h:mm:ss")
    }
    override fun toString() = "${"%.3f".format(lat)} ${"%.3f".format(lng)} ${DATE_FORMATTER.format(time)}"
}

class LocationManager(context: Context) {
    private val locationApi by lazy { LocationServices.getFusedLocationProviderClient(context) }

    fun startListen(intervalMs: Long, listener: (Location) -> Unit) {
        locationApi.requestLocationUpdates( // CUR cache last location
            LocationRequest.create().setFastestInterval(intervalMs).setInterval(intervalMs),
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    listener(
                        Location(
                            result.lastLocation.latitude,
                            result.lastLocation.longitude,
                            result.lastLocation.time
                        )
                    )
                }
            },
            Looper.getMainLooper()
        )
    }
}

class SettingsManager(context: Context) {
    var timeoutMs: Int = 1000
        private set
}
