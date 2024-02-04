package hu.ait.weatherinfo.network

import hu.ait.weatherinfo.data.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query


//http://api.openweathermap.org/geo/1.0/direct?q={city name}&appid={API key}
//Good one: https://api.openweathermap.org/data/2.5/weather?q=Budapest&appid=f3d694bc3e1d44c1ed5a97bd1120e8fe
interface WeatherAPI {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String
    ): WeatherResult
}