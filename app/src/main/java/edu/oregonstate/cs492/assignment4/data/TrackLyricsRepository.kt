package edu.oregonstate.cs492.assignment4.data

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
    ): Result<LyricsResponse?> {
        return withContext(ioDispatcher) {
            try {
                val response = service.getTrackLyrics(trackId, apiKey)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch data"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}