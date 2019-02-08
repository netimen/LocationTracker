package ru.netimen.locationtracker

import android.util.Log

object Logger {
    private const val TAG = "LOCATION_TRACKER"

    fun message(message: String) {
        Log.i(TAG, message)
    }

    fun error(message: String, error: Throwable) {
        Log.e(TAG, "$message $error")
    }
}