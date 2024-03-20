package edu.oregonstate.cs492.assignment4.data

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
        return withContext(ioDispatcher) {
            val response = service.loadMusicSearch(searchQuery, page_size, apiKey)
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
        }
    }

}