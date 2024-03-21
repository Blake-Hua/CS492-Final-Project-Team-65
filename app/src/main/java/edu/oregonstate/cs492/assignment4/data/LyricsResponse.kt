package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson

@JsonClass(generateAdapter = true)
data class LyricsResponse(
    val message: LyricsMessage
)

@JsonClass(generateAdapter = true)
data class LyricsMessage(
    val body: LyricsBody
)

@JsonClass(generateAdapter = true)
data class LyricsBody(
    val lyrics: LyricsData
)

@JsonClass(generateAdapter = true)
data class LyricsData(
    val lyrics_id: Int,
    val restricted: Int,
    val instrumental: Int,
    val lyrics_body: String,
    val lyrics_language: String,
    val script_tracking_url: String,
    val pixel_tracking_url: String,
    val updated_time: String
)

class LyricsJsonAdapter {
    @FromJson
    fun fromJson(lyricsResponse: LyricsResponse): LyricsResponse {
        return lyricsResponse
    }

    @ToJson
    fun toJson(lyricsResponse: LyricsResponse): LyricsResponse {
        return lyricsResponse
    }
}
