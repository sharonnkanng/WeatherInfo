package hu.ait.weatherinfo.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hu.ait.weatherinfo.R
import hu.ait.weatherinfo.data.WeatherResult
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun ResultView(
    weatherResult: WeatherResult,
    onDismiss: () -> Unit
) {

    val resources = LocalContext.current.resources
    val city = resources.getString(R.string.city)
    val weather = resources.getString(R.string.weather)
    val dateandtime = resources.getString(R.string.dateandtime)
    val temperature = resources.getString(R.string.temperature)
    val feellike = resources.getString(R.string.feellike)
    val humidity = resources.getString(R.string.humidity)
    val sunrise = resources.getString(R.string.sunrise)
    val sunset = resources.getString(R.string.sunset)

    val calendar_sunrise = Calendar.getInstance()
    val calendar_sunset = Calendar.getInstance()
    val calendar = Calendar.getInstance()
    val sunRise = weatherResult.sys?.sunrise!!.toLong()
    val sunSet = weatherResult.sys?.sunset!!.toLong()


    calendar_sunrise.setTimeInMillis(sunRise * 1000)
    calendar_sunset.setTimeInMillis(sunSet * 1000)
    calendar.setTimeInMillis(weatherResult.dt?.toLong()!! * 1000)


    val sunRiseText = SimpleDateFormat("HH:mm").format(calendar_sunrise.time)
    val sunSetText = SimpleDateFormat("HH:mm").format(calendar_sunset.time)
    val dateText = SimpleDateFormat("MM.dd.yyyy").format(calendar.time)

    Dialog(
        onDismissRequest = {
            onDismiss()
        })
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                ),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            "https://openweathermap.org/img/w/${
                                weatherResult.weather?.get(0)?.icon
                            }.png"
                        )
                        .crossfade(true)
                        .build(),
                    contentDescription = weatherResult.weather?.get(0)?.main,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Text(text = "${city + weatherResult.name}")
                Text(text = "${weather + weatherResult.weather?.get(0)?.description}")
                Text(text = "${dateandtime +dateText}")


                Text(text = "${temperature + weatherResult.main?.temp}")

                Text(text = "${feellike + weatherResult.main?.feelsLike}")
                Text(text = "${humidity + weatherResult.main?.humidity}")


                Text(text = "${sunrise + sunRiseText}")
                Text(text = "${sunset + sunSetText}")

            }

        }

    }

}