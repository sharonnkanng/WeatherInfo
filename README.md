# WeatherInfo
The goal of this project is to create a Weather Info Application for Android.   
This application fetches weather information from OpenWeatherMap using HTTP requests and displays it to the user.   
During the development process, the project aims to provide practice in network communication, JSON parsing, and the usage of external libraries such as Retrofit and Glide.

## Features

 - Utilize OpenWeatherMap API to fetch weather information.
- Obtain an API key by registering on the OpenWeatherMap website.
  - API Endpoint: [OpenWeatherMap API](http://openweathermap.org/api)

- **City List Screen**:
  - Displays a list of cities.
  - Supports adding and removing cities.
  - Addition of cities can be facilitated by either:
    - A FloatingActionButton that triggers a dialog for entering the city name.
    - A Toolbar with an "Add city" menu.
  - Show appropriate error messages to the user in case of network errors or invalid responses
  - City names do not persist in a database.

- **Weather Details Screen**:
  - Displays weather details for the selected city.
  - Implements network communication to fetch weather information.
  - Weather information includes temperature, humidity, wind speed, etc.
  - Uses appropriate icons/images to represent weather conditions.
  - Icons are downloaded based on the icon field of the retrieved JSON result.

## External Libraries

- Retrofit: For making HTTP requests and handling API communication.
  - [Retrofit Documentation](http://square.github.io/retrofit/)
- Glide (or Coil): For efficient image loading and displaying weather icons.
  - [Glide Documentation](https://coil-kt.github.io/coil/compose/)
 
## Demo

<img src="https://github.com/sharonnkanng/WeatherInfo/assets/94573832/85d1ac4e-d0c1-4df7-a02f-8d067c2e85c7" width="250" height="whatever">
