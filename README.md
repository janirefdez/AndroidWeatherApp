# Android Weather App
[![Kotlin](https://img.shields.io/badge/Kotlin--blue.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android--blue.svg)](https://developer.android.com/)

Weather app made in Kotlin. This app shows the weather at your current location

## Requirements
- Android API 24 - 31
- Gradle 7.2
- Kotlin 1.6.10

## Characteristics
- MVVM 
- Clean Architecture
- Dagger Hilt
- LiveData
- Coroutines
- Retrofit
- Commons IO
- Mockk

## Functionalities

- Get current location.
- Get weather data at current location.
  
Weather data comes from [open weather map API](https://openweathermap.org/current). 

In this case the request used to obtain the weather information is:
```
https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
```

## Notes
I have also done this project for iOS, if you want to see it just click on this [link](https://github.com/janirefdez/IOSWeatherApp) 

## Demo
<img src="https://user-images.githubusercontent.com/20024632/159376057-3a0eb9a0-7d2d-4a82-9cf6-c8372713abb7.gif" width="400">
