package edu.oregonstate.cs492.assignment4.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.assignment4.data.Lyrics
import edu.oregonstate.cs492.assignment4.data.MusicService
import edu.oregonstate.cs492.assignment4.data.TrackLyricsRepository
import kotlinx.coroutines.launch

class LyricsViewModel : ViewModel() {
    private val repository = TrackLyricsRepository(MusicService.create())

    private val _lyrics = MutableLiveData<Lyrics?>(null)
    val lyrics: LiveData<Lyrics?> = _lyrics

    fun loadLyrics(trackId: Int, apiKey: String) {
        viewModelScope.launch {
            val result = repository.loadTrackLyrics(trackId, apiKey)
            _lyrics.value = result.getOrNull()
        }
    }
}