package ru.netimen.locationtracker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val uuidView by lazy { findViewById<TextView>(R.id.uuid) }
    private val timeoutView by lazy { findViewById<TextView>(R.id.timeout) }
    private val locationView by lazy { findViewById<TextView>(R.id.location) }
    private val settingsManager by lazy { SettingsManager(this.applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        uuidView.text = settingsManager.uuid
        timeoutView.text = settingsManager.timeoutS.toString()
        timeoutView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = Unit
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                settingsManager.timeoutS = Integer.valueOf(s.toString())
            }
        })
        locationView.text = Location(65.343, 12.1234, System.currentTimeMillis()).toString()
        LocationManager(this.applicationContext).startListen(10L) {
            locationView.text = it.toString()
        }
    }
}

class Location(val lat: Double, val lng: Double, val timeMs: Long) {
    companion object {
        @SuppressLint("SimpleDateFormat")
        private val DATE_FORMATTER = SimpleDateFormat("hh:mm:ss")
    }

    override fun toString() = "${"%.3f".format(lat)} ${"%.3f".format(lng)} ${DATE_FORMATTER.format(timeMs)}"
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

@Suppress("HasPlatformType")
class SettingsManager(context: Context) {
    companion object {
        private const val KEY_UUID = "uuid"
        private const val KEY_TIMEOUT = "timeout"
    }

    private val prefs = context.getSharedPreferences("locationtracker", Context.MODE_PRIVATE)

    var timeoutMs: Int = prefs.getInt(KEY_TIMEOUT, 10000)
        set(value) {
            field = value
            prefs.edit().putInt(KEY_TIMEOUT, value).apply()
        }

    var timeoutS
        get() = timeoutMs / 1000
        set(value) {
            timeoutMs = value * 1000
        }

    val uuid by lazy {
        if (!prefs.contains(KEY_UUID)) {
            prefs.edit().putString(KEY_UUID, UUID.randomUUID().toString()).apply()
        }
        prefs.getString(KEY_UUID, "")
    }
}
