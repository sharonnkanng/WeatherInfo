package hu.ait.weatherinfo.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.weatherinfo.data.WeatherResult
import hu.ait.weatherinfo.network.WeatherAPI
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherAPI: WeatherAPI
) : ViewModel() {

    var weatherUIState: WeatherUIState by mutableStateOf(WeatherUIState.Init)
    var weatherList by mutableStateOf<List<WeatherResult>>(emptyList())

    fun getWeather(city:String, accessKey: String) {
        // exec network call and change UI states properly..
        weatherUIState = WeatherUIState.Loading

        viewModelScope.launch {
           try {
                val weatherResult = weatherAPI.getWeather(city, "metric",accessKey)
               weatherList += weatherResult
               weatherUIState = WeatherUIState.Success
            } catch (e: Exception) {
                weatherUIState = WeatherUIState.Error(e.message!!)
            }
        }

    }

    fun removeCity(city: String) {
        var remainingCities = emptyList<WeatherResult>()
        for (weather in weatherList) {
            if (weather.name != city) {
                remainingCities += weather
            }
        }
        weatherList = remainingCities
    }

}

sealed interface WeatherUIState {
    object Init : WeatherUIState
    object Loading : WeatherUIState
    object Success : WeatherUIState
    data class Error(val errorMsg:String) : WeatherUIState

}