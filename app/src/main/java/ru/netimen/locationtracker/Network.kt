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

@MainThread
class NetworkManager(private val settingsManager: SettingsManager) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ctsp.cts-group.ru")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(NetworkApi::class.java)

    fun sendLocation(location: Location, onSuccess: (NetworkResponse) -> Unit = {}) {
        Logger.message(logHeader(location))
        service.sendLocation(settingsManager.uuid, location.lat, location.lng)
            .enqueue(object : Callback<NetworkResponse> {
                override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                    Logger.error("${logHeader(location)}, error: ", t)
                }

                override fun onResponse(call: Call<NetworkResponse>, response: Response<NetworkResponse>) {
                    val result = response.body()
                    if (response.isSuccessful && result != null) {
                        Logger.message("${logHeader(location)}, result: $result")
                        onSuccess(result)
                    } else {
                        Logger.error(
                            "$${logHeader(location)}, error: ",
                            IOException(response.message())
                        )
                    }
                }
            })
    }

    private fun logHeader(location: Location) = "sending <$location>"
}

data class NetworkResponse(val success: Int, val timeoutS: Int)

private interface NetworkApi {
    @GET("location")
    fun sendLocation(@Query("id") uuid: String, @Query("lat") lat: Double, @Query("lng") lng: Double): Call<NetworkResponse>
}
