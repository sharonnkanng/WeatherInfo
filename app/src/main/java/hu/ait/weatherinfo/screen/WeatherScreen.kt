package hu.ait.weatherinfo.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hu.ait.weatherinfo.R
import hu.ait.weatherinfo.data.WeatherResult
import hu.ait.weatherinfo.detail.ResultView
import hu.ait.weatherinfo.ui.theme.WeatherInfoTheme
import kotlinx.coroutines.launch
import java.util.Date

private const val API_KEY: String = "d8caa78c1144fa60f493a840d628c576"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    var showAddCityDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val weatherList = weatherViewModel.weatherList

    val resources = LocalContext.current.resources
    val title = resources.getString(R.string.app_name)
    val noItemString = resources.getString(R.string.empty_list)
    val delete = resources.getString(R.string.delete)
    val wait = resources.getString(R.string.wait)
    val error = resources.getString(R.string.error)

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(title)
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                ),
                actions = {
                    IconButton(onClick = {
                        showAddCityDialog = true
                    }) {
                        Icon(Icons.Filled.AddCircle, null)
                    }
                })
        }
    )
    {
        Column(modifier = Modifier.padding(it)) {
            if (showAddCityDialog) {
                AddNewCityForm(
                    { showAddCityDialog = false },
                    onAddCity = { city ->
                        weatherViewModel.getWeather(city, API_KEY)
                        showAddCityDialog = false
                    },
                    { message ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                )
            }
            
            when (weatherViewModel.weatherUIState) {
                is WeatherUIState.Init -> Text(text = noItemString, modifier = Modifier.padding(20.dp))
                is WeatherUIState.Loading -> CircularProgressIndicator()
                is WeatherUIState.Success ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                    ) {
                        items(weatherList) { weatherItem ->
                            ItemCard(weatherItem = weatherItem,
                                onRemoveItem = {
                                    weatherViewModel.removeCity(weatherItem.name!!)
                                })
                        }
                    }

                is WeatherUIState.Error ->
                    Text(
                        text =
                                "${error + (weatherViewModel.weatherUIState as WeatherUIState.Error).errorMsg}"
                    )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCityForm(
    onDismiss: () -> Unit,
    onAddCity: (String) -> Unit,
    onFormReady: (String) -> Unit = {}
) {
    var newCity by rememberSaveable {
        mutableStateOf("")
    }

    val resources = LocalContext.current.resources
    val cityAdded = resources.getString(R.string.cityAdded)
    val add = resources.getString(R.string.add)
    val cancel = resources.getString(R.string.cancel)
    val cityName = resources.getString(R.string.cityName)
    val addCity = resources.getString(R.string.addCity)


    Dialog(
        onDismissRequest = {
            onDismiss()
        }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        )
        {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            {
                item {
                    Column(
                        Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(10.dp)
                    ) {
                        Text(addCity)
                        OutlinedTextField(
                            label = { Text(cityName) },
                            modifier = Modifier.fillMaxWidth(),
                            value = newCity,
                            onValueChange = {
                                newCity = it
                            })
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.Center
                        ){

                            Button(onClick = {
                                // newCity exists in API call
                               if (!newCity.isNullOrEmpty()) {
                                   onAddCity(newCity)
                                   onFormReady(newCity + cityAdded)
                               }
                            }) {
                                Text(add)
                            }
                            Spacer(modifier = Modifier.fillMaxSize(0.1f))
                            Button(onClick = {
                                onDismiss()
                            }) {
                                Text(cancel)
                            }
                        }
                    }

                }

            }
        }
    }
}

@Composable
fun ItemCard(
    weatherItem: WeatherResult,
    onRemoveItem: () -> Unit = {}
) {

    var resources = LocalContext.current.resources
    val celcius = resources.getString(R.string.celcius)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        var showDetailDialog by rememberSaveable {
            mutableStateOf(false)
        }
        Row(
            modifier = Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            "https://openweathermap.org/img/w/${
                                weatherItem.weather?.get(0)?.icon
                            }.png"
                        )
                        .crossfade(true)
                        .build(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = weatherItem.name!!, style = TextStyle(fontSize = 18.sp),
                    fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.fillMaxSize(0.02f))
                Text(text = weatherItem.main!!.temp.toString() + celcius)
                Spacer(modifier = Modifier.fillMaxSize(0.2f))
                    Button(onClick = {
                        showDetailDialog = true
                    }) {
                        Text(
                            text = "Details",
                            style = TextStyle(fontSize = 18.sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
            Spacer(modifier = Modifier.fillMaxSize(0.1f))
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier
                            .clickable {
                                onRemoveItem()
                            }
                            .size(30.dp),
                        tint = Color.Red
                    )
            }
            if (showDetailDialog) {
                Spacer(modifier = Modifier.height(10.dp))
                //TODO
                ResultView(weatherItem, onDismiss = { showDetailDialog = false })

            }
        }
    }

