package edu.oregonstate.cs492.assignment4.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

import edu.oregonstate.cs492.assignment4.data.WeatherDao

/**
 * This class manages data operations associated with the OpenWeather's current weather API.
 */
class CurrentWeatherRepository (
    private val service: OpenWeatherService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /*
     * These three properties are used to implement a basic caching strategy, where an API call
     * is only executed if the requested location or units don't match the ones from the previous
     * API call.
     */
    private var currentLocation: String? = null
    private var currentUnits: String? = null
    private var cachedWeather: ForecastPeriod? = null

    /*
     * These values are used to help measure the age of the cached forecast.  See the Kotlin
     * documentation on time measurement for details:
     *
     * https://kotlinlang.org/docs/time-measurement.html
     */
    private val cacheMaxAge = 5.minutes
    private val timeSource = TimeSource.Monotonic
    private var timeStamp = timeSource.markNow()

    /**
     * This method executes a new query to the OpenWeather API's current weather method.  It
     * is a suspending function and executes within the coroutine context specified by the
     * `dispatcher` argument to the Repository class's constructor.
     *
     * @param location Specifies the location for which to fetch weather data.  For US cities,
     *   this should be specified as "<city>,<state>,<country>" (e.g. "Corvallis,OR,US"), while
     *   for international cities, it should be specified as "<city>,<country>" (e.g. "London,GB").
     * @param units Specifies the type of units that should be returned by the OpenWeather API.
     *   Can be one of: "standard", "metric", and "imperial".
     * @param apiKey Should be a valid OpenWeather API key.
     *
     * @return Returns a Kotlin Result object wrapping the [ForecastPeriod] object that
     *   represents the fetched forecast.  If the API query is unsuccessful for some reason, the
     *   Exception associated with the Result object will provide more info about why the query
     *   failed.
     */
    suspend fun loadCurrentWeather(
        location: String?,
        units: String?,
        apiKey: String
    ) : Result<ForecastPeriod?> {
        /*
         * If we can do so, return the cached weather without making a network call.  Otherwise,
         * make an API call to fetch the weather and cache it.
         */
        return if (shouldFetch(location, units)) {
            withContext(ioDispatcher) {
                try {
                    val response = service.loadCurrentWeather(location, units, apiKey)
                    if (response.isSuccessful) {
                        cachedWeather = response.body()
                        timeStamp = timeSource.markNow()
                        currentLocation = location
                        currentUnits = units
                        Result.success(cachedWeather)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        } else {
            Result.success(cachedWeather!!)
        }
    }

    /**
     * Determines whether the weather should be fetched by making a new HTTP call or whether
     * the cached weather can be returned.  The cached weather should be used if the requested
     * location and units match the ones corresponding to the cached weather and the cached
     * weather is not stale.
     *
     * @param location The location for which the weather is to be potentially fetched, as passed
     *   to `loadCurrentWeather()`.
     * @param units The type of units to use in weather that is to be potentially fetched,
     *   as passed to `loadCurrentWeather()`.
     *
     * @return Returns true if the weather should be fetched and false if the cached version
     *   should be used.
     */
    private fun shouldFetch(location: String?, units: String?): Boolean =
        cachedWeather == null
        || location != currentLocation
        || units != currentUnits
        || (timeStamp + cacheMaxAge).hasPassedNow()
}