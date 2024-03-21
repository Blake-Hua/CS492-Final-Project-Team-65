package edu.oregonstate.cs492.assignment4.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.MusicService

import kotlinx.coroutines.*

class TrackLyricsFragment : Fragment(R.layout.fragment_track_lyrics) {

    private var coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var musicService: MusicService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lyricsTV: TextView = view.findViewById(R.id.tv_lyrics)

        // Initialize the MusicService
        musicService = MusicService.create()

        // Log for debugging
        Log.d("TrackLyricsFragment", "onViewCreated: MusicService initialized.")

        // Dummy track ID, replace with actual track ID
        val trackId = 31635577
        fetchLyrics(trackId, lyricsTV)
//        // Retrieve track ID from arguments
//        val trackId = arguments?.let { TrackLyricsFragmentArgs.fromBundle(it).trackId }
//        if (trackId != null) {
//            Log.d("TrackLyricsFragment", "onViewCreated: Fetching lyrics for track ID $trackId.")
//            fetchLyrics(trackId, lyricsTV)
//        } else {
//            Log.e("TrackLyricsFragment", "onViewCreated: No track ID provided.")
//            lyricsTV.text = "Error: No track ID provided."
//        }

    }

    private fun fetchLyrics(trackId: Int, lyricsTV: TextView) {
        Log.d("TrackLyricsFragment", "fetchLyrics: Fetching lyrics for track ID $trackId.")
        coroutineScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    Log.d("TrackLyricsFragment", "fetchLyrics: Making network request.")
                    val apiKey = getString(R.string.music_api_key)
                    musicService.getTrackLyrics(trackId, apiKey)
                }
                if (response.isSuccessful && response.body() != null) {
                    val lyricsData = response.body()?.message?.body?.lyrics
                    Log.d("TrackLyricsFragment", "fetchLyrics: Lyrics fetched successfully.")
                    Log.d("TrackLyricsFragment", "fetchLyrics: $lyricsData")
                    lyricsTV.text = lyricsData?.lyrics_body ?: "Lyrics not found."
                    Log.d("TrackLyricsFragment", "fetchLyrics: $lyricsTV")
                } else {
                    // Log error details
                    Log.e("TrackLyricsFragment", "fetchLyrics: Error fetching lyrics. Response code: ${response.code()}, Message: ${response.message()}")
                    lyricsTV.text = "Error fetching lyrics."
                }
            } catch (e: Exception) {
                // Log exception
                Log.e("TrackLyricsFragment", "fetchLyrics: Exception occurred.", e)
                lyricsTV.text = "Error fetching lyrics due to an exception."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel() // Cancel coroutines when the view is destroyed
        Log.d("TrackLyricsFragment", "onDestroyView: CoroutineScope canceled.")
    }
}
