package ru.netimen.locationtracker

import android.content.Context
import android.os.BatteryManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryHelper @Inject constructor(private val context: Context) {
    private val batteryManager by lazy { context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager }
    val batteryLevel get() = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
}