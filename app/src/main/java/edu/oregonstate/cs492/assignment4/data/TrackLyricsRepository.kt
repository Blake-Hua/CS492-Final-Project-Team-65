package edu.oregonstate.cs492.assignment4.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackLyricsRepository (
    private val service: MusicService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
){
    suspend fun loadTrackLyrics(
        trackId: Int,
        apiKey: String
    ): Result<Lyrics?> {
        return withContext(ioDispatcher) {
            try {
                val response = service.getTrackLyrics(trackId, apiKey)
                Log.d("TrackLyricsRepository", "loadTrackLyrics: $response")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("TrackLyricsRepository", "loadTrackLyrics: $body")
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Log.d("TrackLyricsRepository", "loadTrackLyrics: Empty response")
                        Result.failure(Exception("Empty response"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch data"))
                }
            } catch (e: Exception) {
                Log.d("TrackLyricsRepository", "loadTrackLyrics: $e, ")
                Result.failure(e)
            }
        }
    }
}