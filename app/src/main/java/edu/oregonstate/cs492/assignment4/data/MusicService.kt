package edu.oregonstate.cs492.assignment4.data

import android.adservices.adid.AdId
import android.util.Log
import com.squareup.moshi.Moshi
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This is a Retrofit service interface encapsulating communication with the OpenWeather API.
 */
interface MusicService {
    /**
     * I'm using the API http://api.musixmatch.com/ws/1.1/track.search to get a list of tracks based on
     * the user query.
     */
    @GET("track.search")
    suspend fun loadMusicSearch(
        @Query("q_track") searchQuery: String?,
        @Query("q_artist") searchArtist: String?,
        @Query("page_size") page_size: String?,
        @Query("apikey") apiKey: String
    ) : Response<TrackResults>

    @GET("track.lyrics.get")
    suspend fun getTrackLyrics(
        @Query("track_id") trackId: Int,
        @Query("apikey") apiKey: String
    ) : Response<Lyrics>

    companion object {
        private const val BASE_URL = "https://api.musixmatch.com/ws/1.1/"

        /**
         * This method can be called as `OpenWeatherService.create()` to create an object
         * implementing the OpenWeatherService interface and which can be used to make calls to
         * the OpenWeather API.
         */
        fun create() : MusicService {
            Log.d("MusicService", "create")
            val moshi = Moshi.Builder()
                .add(MusicTrackJsonAdapter())
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MusicService::class.java)
        }
    }
}