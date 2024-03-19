package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

/**
 * This class encapsulates data about a single forecast period fetched from the OpenWeather API's
 * 5-day/3-hour forecast.  It also represents the response from the current weather API.  It does
 * not directly correspond to the JSON data.  The classes below are used for JSON parsing, and
 * information from them is used by the custom JSON adapter at the bottom of this file to
 * construct this class.
 */
data class ForecastPeriod(
    val epoch: Int,
    val temp: Int,
    val highTemp: Int,
    val lowTemp: Int,
    val pop: Int,
    val cloudCover: Int,
    val windSpeed: Int,
    val windDirDeg: Int,
    val description: String,
    val iconUrl: String,
    val tzOffsetSec: Int
)

/* ******************************************************************************************
 * Below is a set of classes used to parse the JSON response from the OpenWeather API into
 * a ForecastPeriod object.  The first several classes are designed to match the structure
 * of one element of the `list` field in the OpenWeather 5-day forecast API's JSON response.
 * The last is a custom type adapter that can be used with Moshi to parse OpenWeather JSON
 * directly into a ForecastPeriod object.
 * ******************************************************************************************/

/**
 * This class represents an item in the `list` field of the JSON response from the OpenWeather API.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherListJson(
    val dt: Int,
    val pop: Double?,
    val main: OpenWeatherListMainJson,
    val clouds: OpenWeatherListCloudsJson,
    val wind: OpenWeatherListWindJson,
    val weather: List<OpenWeatherListWeatherJson>,
    val timezone: Int?
)

/**
 * This class represents the `list[i].main` field of the JSON response from the OpenWeather API.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherListMainJson(
    val temp: Double,
    val temp_min: Double,
    val temp_max: Double
)

/**
 * This class represents the `list[i].clouds` field of the JSON response from the OpenWeather API.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherListCloudsJson(
    val all: Int
)

/**
 * This class represents the `list[i].wind` field of the JSON response from the OpenWeather API.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherListWindJson(
    val speed: Double,
    val deg: Int
)

/**
 * This class represents the `list[i].weather[j]` field of the JSON response from the OpenWeather
 * API.
 */
@JsonClass(generateAdapter = true)
data class OpenWeatherListWeatherJson(
    val description: String,
    val icon: String
)

/**
 * This class is a custom JSON adapter for use with Moshi.  It uses the classes above to represent
 * and parse the JSON response from the OpenWeather API, then it takes data from the parsed JSON
 * and uses it to build a [ForecastPeriod] object, which becomes the ultimate return value from the
 * JSON parsing.
 */
class OpenWeatherForecastPeriodJsonAdapter {
    @FromJson
    fun forecastPeriodFromJson(list: OpenWeatherListJson) = ForecastPeriod(
        epoch = list.dt,
        temp = list.main.temp.toInt(),
        highTemp = list.main.temp_max.toInt(),
        lowTemp = list.main.temp_min.toInt(),
        pop = ((list.pop ?: 0.0) * 100).toInt(),
        cloudCover = list.clouds.all,
        windSpeed = list.wind.speed.toInt(),
        windDirDeg = list.wind.deg,
        description = list.weather[0].description,
        iconUrl = "https://openweathermap.org/img/wn/${list.weather[0].icon}@4x.png",
        tzOffsetSec = list.timezone ?: 0
    )

    @ToJson
    fun forecastPeriodToJson(forecastPeriod: ForecastPeriod): String {
        throw UnsupportedOperationException("encoding ForecastPeriod to JSON is not supported")
    }
}