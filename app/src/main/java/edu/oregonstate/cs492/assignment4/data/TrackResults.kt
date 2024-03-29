package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This class is used to help parse the JSON data returned by the MusixMatch API's track.search
 * endpoint.
 */

@JsonClass(generateAdapter = true)
data class TrackResults(
    @Json(name = "message") val message: Message,
    val trackList: List<Track> = message.body.track_list.map { it.track }

)
//    val trackList : List<Track> = body.track_list
//    @Json(name = "track_list") val trackList: List<Track>

//track_id misssing at
// message.body.track_list[1]