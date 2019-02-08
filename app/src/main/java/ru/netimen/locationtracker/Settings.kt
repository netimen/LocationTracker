package ru.netimen.locationtracker

import android.content.Context
import java.util.*

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