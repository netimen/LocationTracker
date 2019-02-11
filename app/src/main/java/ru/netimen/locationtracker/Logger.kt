package ru.netimen.locationtracker

import android.content.Context
import android.util.Log
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Logger @Inject constructor(context: Context) {
    companion object {
        private const val TAG = "LOCATION_TRACKER"
    }

    init {
        Fabric.with(context, Crashlytics())
    }

    fun message(message: String) {
        Log.i(TAG, message)
    }

    fun error(message: String, error: Throwable) {
        Log.e(TAG, "$message $error")
    }
}