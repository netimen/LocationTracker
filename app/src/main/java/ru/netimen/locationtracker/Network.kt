package ru.netimen.locationtracker

import android.support.annotation.MainThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@MainThread
@Singleton
class NetworkManager @Inject constructor(private val settingsManager: SettingsManager, private val logger: Logger) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ctsp.cts-group.ru")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(NetworkApi::class.java)

    fun sendLocation(location: Location, batteryLevel: Int, onSuccess: (NetworkResponse) -> Unit = {}) {
        logger.message("${logHeader(location)} battery: $batteryLevel")
        service.sendLocation(settingsManager.uuid, location.lat, location.lng, batteryLevel)
            .enqueue(object : Callback<NetworkResponse> {
                override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                    logger.error("${logHeader(location)}, error: ", t)
                }

                override fun onResponse(call: Call<NetworkResponse>, response: Response<NetworkResponse>) {
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        logger.message("${logHeader(location)}, result: $result")
                        onSuccess(result)
                    } else {
                        logger.error("$${logHeader(location)}, error: ", IOException(response.message()))
                    }
                }
            })
    }

    private fun logHeader(location: Location) = "sending <$location>"
}

data class NetworkResponse(val success: Int, val timeout: Int)

private interface NetworkApi {
    @GET("location")
    fun sendLocation(@Query("id") uuid: String, @Query("lat") lat: Float, @Query("lng") lng: Float, @Query("level") batteryLevel: Int): Call<NetworkResponse>
}
