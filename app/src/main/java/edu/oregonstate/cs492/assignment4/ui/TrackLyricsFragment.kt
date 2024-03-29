package edu.oregonstate.cs492.assignment4.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.MusicService
import edu.oregonstate.cs492.assignment4.data.TrackLyricsRepository

import kotlinx.coroutines.*

class TrackLyricsFragment : Fragment(R.layout.fragment_track_lyrics) {

    private val args: TrackLyricsFragmentArgs by navArgs()
    private val repository = TrackLyricsRepository(MusicService.create())
    private val viewModel: LyricsViewModel by viewModels()

    private lateinit var lyricsTV: TextView

    var lyricsUrl: String? = null;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lyricsTV = view.findViewById(R.id.tv_lyrics)

        val apiKey = getString(R.string.music_api_key)

        viewModel.loadLyrics(args.track, apiKey)

        viewModel.lyrics.observe(viewLifecycleOwner) { lyrics ->
            if (lyrics != null) {
                lyricsTV.text = lyrics.lyricsBody
                lyricsUrl = lyrics.scriptTrackingUrl
            }
        }

    }
    private fun viewOnMusic() {
        val url = Uri.parse(lyricsUrl)
        val intent = Intent(Intent.ACTION_VIEW, url)
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            /* Handle activity not found */
        }
    }



}
