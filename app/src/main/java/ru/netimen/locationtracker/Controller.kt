package ru.netimen.locationtracker

import javax.inject.Inject
import javax.inject.Singleton

@Suppress("HasPlatformType")
@Singleton
class Controller @Inject constructor(
    private val settingsManager: SettingsManager,
    private val networkManager: NetworkManager,
    private val locationManager: LocationManager,
    private val batteryHelper: BatteryHelper,
    private val logger: Logger
) {
    var onTimeoutChanged: ((Int) -> Unit)? = null
        set(value) {
            field = value
            value?.invoke(settingsManager.timeoutS)
        }

    var onLastLocationChanged: ((Location?) -> Unit)? = null
        set(value) {
            field = value
            value?.invoke(settingsManager.lastSentLocation)
        }

    val uuid get() = settingsManager.uuid

    @Suppress("NAME_SHADOWING")
    fun changeTimeout(timeoutS: Int) {
        val timeoutS = Math.min(timeoutS, SettingsManager.MIN_TIMEOUT_S)
        if (timeoutS == settingsManager.timeoutS) return
        logger.message("change timeout from ${settingsManager.timeoutS} to $timeoutS")

        settingsManager.timeoutS = timeoutS
        onTimeoutChanged?.invoke(timeoutS)
        start()
    }

    fun start(timeoutMs: Int = settingsManager.timeoutMs) {
        locationManager.startListen(timeoutMs) { location ->
            networkManager.sendLocation(location, batteryHelper.batteryLevel) {
                settingsManager.lastSentLocation = location
                onLastLocationChanged?.invoke(location)
                if (it.timeout > 0) changeTimeout(it.timeout)
            }
        }
    }
}