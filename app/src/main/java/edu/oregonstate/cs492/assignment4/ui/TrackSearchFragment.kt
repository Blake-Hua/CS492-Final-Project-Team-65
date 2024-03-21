package edu.oregonstate.cs492.assignment4.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.Track
import edu.oregonstate.cs492.assignment4.util.LoadingStatus

class TrackSearchFragment : Fragment(R.layout.fragment_track_search) {
    private val viewModel: TrackSearchViewModel by viewModels()
    private val adapter = TrackResultsAdapter(::onTrackClick)

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBoxArtist: EditText = view.findViewById(R.id.et_search_box_artist)
        val searchBoxET: EditText = view.findViewById(R.id.et_search_box)
        val searchBtn: Button = view.findViewById(R.id.btn_search)

        searchErrorTV = view.findViewById(R.id.tv_search_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        searchResultsListRV = view.findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = adapter

        viewModel.searchResults.observe(viewLifecycleOwner) {
            searchResults -> adapter.updateTrackList(searchResults)
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) {
            loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.SUCCESS -> {
                    searchResultsListRV.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            error -> searchErrorTV.text = error
        }

        val menuHost: MenuHost = requireActivity()
//        menuHost.addMenuProvider(
//            object : MenuProvider {
//                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                    menuInflater.inflate(R.menu.track_search_menu , menu)
//                }
//
//                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                    return when (menuItem.itemId) {
//                        R.id.action_bookmarks -> {
//                            val directions = TrackSearchFragmentDirections.navigateToLyrics()
//                            findNavController().navigate(directions)
//                            true
//                        }
//                        else -> false
//                    }
//                }
//            },
//            viewLifecycleOwner,
//            Lifecycle.State.STARTED
//        )

        searchBtn.setOnClickListener {
            val searchArtist = searchBoxArtist.text.toString()
            val searchQuery = searchBoxET.text.toString()
            val apiKey = getString(R.string.music_api_key)
            viewModel.loadMusicSearch(searchQuery, searchArtist,"10", apiKey)
        }

    }

    private fun onTrackClick(track: Track) {
        val url = track.track_share_url
        if (url.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } else {
            // Optionally handle the case where there is no URL (e.g., show a message)
        }
//        val directions = TrackSearchFragmentDirections.navigateToLyrics()
//        val directions = TrackSearchFragmentDirections.navigateToLyrics(track.track_id)
//        findNavController().navigate(directions)
    }

}