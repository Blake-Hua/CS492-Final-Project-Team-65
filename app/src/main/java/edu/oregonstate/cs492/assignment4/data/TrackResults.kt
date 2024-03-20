package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This class is used to help parse the JSON data returned by the MusixMatch API's track.search
 * endpoint.
 */

@JsonClass(generateAdapter = true)
data class TrackResults(
    @Json(name = "track_list") val trackList: List<Track>
)