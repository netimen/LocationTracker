package ru.netimen.locationtracker

import android.content.Context
import android.os.BatteryManager

class BatteryHelper(private val context: Context) {
    private val batteryManager by lazy { context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager }
    val batteryLevel get() = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
}