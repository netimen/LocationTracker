package ru.netimen.locationtracker

import android.content.Context
import com.google.gson.Gson
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("HasPlatformType")
@Singleton
class SettingsManager @Inject constructor(context: Context) {
    private val prefs = context.getSharedPreferences("locationtracker", Context.MODE_PRIVATE)
    private val gson = Gson()

    var timeoutMs: Int = prefs.getInt(KEY_TIMEOUT, MIN_TIMEOUT_S * 1000)
        set(value) {
            field = value
            prefs.edit().putInt(KEY_TIMEOUT, value).apply()
        }

    var timeoutS
        get() = timeoutMs / 1000
        set(value) {
            timeoutMs = value * 1000
        }

    var lastSentLocation: Location? = readObject(KEY_LOCATION, Location::class.java)
        set(value) {
            field = value
            storeObject(KEY_LOCATION, value)
        }

    val uuid by lazy {
        if (!prefs.contains(KEY_UUID)) {
            prefs.edit().putString(KEY_UUID, UUID.randomUUID().toString()).apply()
        }
        prefs.getString(KEY_UUID, "")
    }

    private fun storeObject(key: String, value: Any?) = prefs.edit().putString(key, gson.toJson(value)).apply()
    private fun <T> readObject(key: String, cls: Class<T>):T? = gson.fromJson(prefs.getString(key, ""), cls)

    companion object {
        private const val KEY_UUID = "uuid"
        private const val KEY_TIMEOUT = "timeout"
        private const val KEY_LOCATION = "location"
        const val MIN_TIMEOUT_S = 5 // empirical value for FusedLocationProvider
    }
}