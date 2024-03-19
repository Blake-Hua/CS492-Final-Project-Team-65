package edu.oregonstate.cs492.assignment4.util

import java.util.Calendar
import java.util.TimeZone

private const val FIVE_DAY_FORECAST_DEFAULT_TZ = "UTC"
private const val TZ_OFFSET_FORMAT_STR = "GMT%0+3d:%02d"

/**
 * This function computes an actual date/time in the correct timezone from the epoch and timezone
 * offset (in seconds) returned by the OpenWeather API.  The date/time is returned as a Calendar.
 */
fun openWeatherEpochToDate(epoch: Int, tzOffsetSec: Int): Calendar {
    val date = Calendar.getInstance(TimeZone.getTimeZone(FIVE_DAY_FORECAST_DEFAULT_TZ))
    date.timeInMillis = epoch.toLong() * 1000L
    val tzOffsetHours = tzOffsetSec / 3600
    val tzOffsetMin = (Math.abs(tzOffsetSec) % 3600) / 60
    val localTZId = TZ_OFFSET_FORMAT_STR.format(tzOffsetHours, tzOffsetMin)
    date.timeZone = TimeZone.getTimeZone(localTZId)
    return date
}