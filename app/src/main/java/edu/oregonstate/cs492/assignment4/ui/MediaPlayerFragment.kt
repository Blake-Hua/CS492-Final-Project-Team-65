package edu.oregonstate.cs492.assignment4.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.ForecastPeriod
import java.lang.Exception

/**
 * This fragment represents the "current weather" screen.
 */
class MediaPlayerFragment : Fragment(R.layout.fragment_media_player) {

    private var mp: MediaPlayer? = null
    private var currentSong = mutableListOf(R.raw.safe_and_sound)

    private lateinit var musicPlayer: View

    private lateinit var fabPlay: FloatingActionButton
    private lateinit var fabPause: FloatingActionButton
    private lateinit var fabStop: FloatingActionButton
    private lateinit var seekbar: SeekBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        musicPlayer = view.findViewById(R.id.ll_wrapper_main)

        fabPlay = musicPlayer.findViewById(R.id.fab_play)
        fabPause = musicPlayer.findViewById(R.id.fab_pause)
        fabStop = musicPlayer.findViewById(R.id.fab_stop)
        seekbar = musicPlayer.findViewById(R.id.seekbar)

        controlSound(currentSong[0])
    }

    private fun controlSound(id: Int){
        fabPlay.setOnClickListener{
            if (mp == null){
                mp = MediaPlayer.create(requireContext(), id)
                Log.d("MainActivity", "ID: ${mp!!.audioSessionId}")

                initialiseSeekBar()
            }
            mp?.start()
        }

        fabPause.setOnClickListener {
            if (mp !== null) mp?.pause()

        }

        fabStop.setOnClickListener {
            if (mp !== null) {
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
            }
        }

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

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