package edu.oregonstate.cs492.assignment4.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.oregonstate.cs492.assignment4.data.TrackSearchRepository
import edu.oregonstate.cs492.assignment4.data.TrackResults
import edu.oregonstate.cs492.assignment4.util.LoadingStatus
import edu.oregonstate.cs492.assignment4.data.MusicService
import edu.oregonstate.cs492.assignment4.data.Track
import kotlinx.coroutines.launch

class TrackSearchViewModel : ViewModel() {
    private val repository = TrackSearchRepository(MusicService.create())

    private val _searchResults = MutableLiveData<TrackResults?>(null)
    val searchResults: LiveData<TrackResults?> = _searchResults

    private val _loadingStatus = MutableLiveData<LoadingStatus>(LoadingStatus.SUCCESS)
    val loadingStatus: LiveData<LoadingStatus> = _loadingStatus

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    fun loadMusicSearch(searchQuery: String?, page_size: String?, apiKey: String) {
        Log.d("TrackSearchViewModel", "loadMusicSearch: $searchQuery")
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = repository.loadMusicSearch(searchQuery, page_size, apiKey)
            Log.d("TrackSearchViewModel", "loadMusicSearch: $result")

            _searchResults.value = result.getOrNull()
            _error.value = result.exceptionOrNull()?.message

            _loadingStatus.value = when (result.isSuccess) {
                true -> LoadingStatus.SUCCESS
                false -> LoadingStatus.ERROR
            }
        }
    }
}