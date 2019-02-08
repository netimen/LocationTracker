package ru.netimen.locationtracker

import android.content.Context
import android.util.Log
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

object Logger {
    private const val TAG = "LOCATION_TRACKER"

    fun message(message: String) {
        Log.i(TAG, message)
    }

    fun error(message: String, error: Throwable) {
        Log.e(TAG, "$message $error")
    }

    fun setup(context: Context) {
        Fabric.with(context, Crashlytics())
    }
}