package edu.oregonstate.cs492.assignment4.data

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.minutes
import kotlin.time.TimeSource

class TrackSearchRepository(
    private val service: MusicService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadMusicSearch(
        searchQuery: String?,
        page_size: String?,
        apiKey: String
    ) : Result<TrackResults?> {
        Log.d("TrackSearchRepository", "loadMusicSearch: $searchQuery")
        return withContext(ioDispatcher) {
            Log.d("TrackSearchRepository", "loadMusicSearch: $searchQuery")
            try { // I'm using the API http://api.musixmatch.com/ws/1.1/track.search to get a list of tracks based on
                // the user query.
                val response = service.loadMusicSearch(searchQuery, page_size, apiKey)
                Log.d("TrackSearchRepository", "loadMusicSearch: $response")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("TrackSearchRepository", "loadMusicSearch: $body")
                    if (body != null) {
                        Result.success(body)
                    } else {
                        Result.failure(Exception("Empty response"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch data"))
                }
            } catch (e: Exception) {
                Log.d("TrackSearchRepository", "loadMusicSearch: $e, ")
                Result.failure(e)
            }
        }
    }

}