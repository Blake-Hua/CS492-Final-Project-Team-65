package edu.oregonstate.cs492.assignment4.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.ForecastPeriod
import edu.oregonstate.cs492.assignment4.data.Song
import java.lang.Exception

/**
 * This fragment represents the "current weather" screen.
 */
class MediaPlayerFragment : Fragment(R.layout.fragment_media_player) {

    private var mp: MediaPlayer? = null
    private var currentSong = mutableListOf(R.raw.safe_and_sound)

    private lateinit var musicPlayer: View
    private lateinit var songsSpinner: Spinner

    private lateinit var fabPlay: FloatingActionButton
    private lateinit var fabPause: FloatingActionButton
    private lateinit var fabStop: FloatingActionButton
    private lateinit var seekbar: SeekBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val songsRecyclerView: RecyclerView = view.findViewById(R.id.songsRecyclerView)

        val songs = listOf(
            Song(R.raw.safe_and_sound, "Safe and Sound"),
            Song(R.raw.sickomode, "Sicko Mode"),
            Song(R.raw.sunflower, "Sunflower")
        )
//
//        val adapter = MediaPlayerAdapter(songs) { song ->
//            controlSound(song.resourceId)
//        }

//        songsRecyclerView.adapter = adapter
//        songsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        musicPlayer = view.findViewById(R.id.ll_wrapper_main)

        fabPlay = musicPlayer.findViewById(R.id.fab_play)
        fabPause = musicPlayer.findViewById(R.id.fab_pause)
        fabStop = musicPlayer.findViewById(R.id.fab_stop)
        seekbar = musicPlayer.findViewById(R.id.seekbar)
        songsSpinner = view.findViewById(R.id.songsSpinner)

        setupSpinner(songs)

        controlSound(currentSong[0])


    }

    private fun setupSpinner(songs: List<Song>) {
        // Create an ArrayAdapter using a simple spinner layout and the song titles
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, songs.map { it.title })
        songsSpinner.adapter = adapter

        songsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Safely check if the fragment is added to its activity
                if (isAdded) {
                    controlSound(songs[position].resourceId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Optional: Handle the case where no selection is made
            }
        }
    }

    private fun controlSound(id: Int){    fabPlay.setOnClickListener {
        // Check if MediaPlayer is initialized or if the song has changed
        if (mp == null || currentSong[0] != id) {
            // Release any existing MediaPlayer resources
            mp?.stop()
            mp?.reset()
            mp?.release()

            // Create a new MediaPlayer instance for the new song
            mp = MediaPlayer.create(requireContext(), id)
            currentSong[0] = id

            initialiseSeekBar()
        }
        mp?.start()
    }

        fabPause.setOnClickListener {
            mp?.pause()
        }

        fabStop.setOnClickListener {
            if (mp != null) {
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
            }
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


    }

    private fun initialiseSeekBar() {
        seekbar.max = mp!!.duration

        val handler = Handler()
        handler.postDelayed(object: Runnable {
            override fun run() {
                try {
                    seekbar.progress = mp!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception){
                    seekbar.progress = 0
                }
            }
        }, 0)

    }
}