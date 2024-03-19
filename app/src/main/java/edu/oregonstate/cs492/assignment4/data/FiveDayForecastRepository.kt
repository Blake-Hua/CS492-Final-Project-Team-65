package edu.oregonstate.cs492.assignment4.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

/**
 * This class manages data operations associated with the OpenWeather API 5-day/3-hour forecast.
 */
class FiveDayForecastRepository (
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
    private var cachedForecast: FiveDayForecast? = null

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
     * This method executes a new query to the OpenWeather API's 5-day/3-hour forecast method.  It
     * is a suspending function and executes within the coroutine context specified by the
     * `dispatcher` argument to the Repository class's constructor.
     *
     * @param location Specifies the location for which to fetch forecast data.  For US cities,
     *   this should be specified as "<city>,<state>,<country>" (e.g. "Corvallis,OR,US"), while
     *   for international cities, it should be specified as "<city>,<country>" (e.g. "London,GB").
     * @param units Specifies the type of units that should be returned by the OpenWeather API.
     *   Can be one of: "standard", "metric", and "imperial".
     * @param apiKey Should be a valid OpenWeather API key.
     *
     * @return Returns a Kotlin Result object wrapping the [FiveDayForecast] object that
     *   represents the fetched forecast.  If the API query is unsuccessful for some reason, the
     *   Exception associated with the Result object will provide more info about why the query
     *   failed.
     */
    suspend fun loadFiveDayForecast(
        location: String?,
        units: String?,
        apiKey: String
    ) : Result<FiveDayForecast?> {
        /*
         * If we can do so, return the cached forecast without making a network call.  Otherwise,
         * make an API call to fetch the forecast and cache it.
         */
        return if (shouldFetch(location, units)) {
            withContext(ioDispatcher) {
                try {
                    val response = service.loadFiveDayForecast(location, units, apiKey)
                    if (response.isSuccessful) {
                        cachedForecast = response.body()
                        timeStamp = timeSource.markNow()
                        currentLocation = location
                        currentUnits = units
                        Result.success(cachedForecast)
                    } else {
                        Result.failure(Exception(response.errorBody()?.string()))
                    }
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
        } else {
            Result.success(cachedForecast!!)
        }
    }

    /**
     * Determines whether the forecast should be fetched by making a new HTTP call or whether
     * the cached forecast can be returned.  The cached forecast should be used if the requested
     * location and units match the ones corresponding to the cached forecast and the cached
     * forecast is not stale.
     *
     * @param location The location for which a forecast is to be potentially fetched, as passed
     *   to `loadFiveDayForecast()`.
     * @param units The type of units to use in a forecast that is to be potentially fetched,
     *   as passed to `loadFiveDayForecast()`.
     *
     * @return Returns true if the forecast should be fetched and false if the cached version
     *   should be used.
     */
    private fun shouldFetch(location: String?, units: String?): Boolean =
        cachedForecast == null
        || location != currentLocation
        || units != currentUnits
        || (timeStamp + cacheMaxAge).hasPassedNow()
}