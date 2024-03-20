package edu.oregonstate.cs492.assignment4.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.oregonstate.cs492.assignment4.R
import edu.oregonstate.cs492.assignment4.data.Track
import edu.oregonstate.cs492.assignment4.data.TrackResults


class TrackResultsAdapter(
    private val onTrackClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackResultsAdapter.ViewHolder>() {
    var trackList: List<Track> = listOf()


    fun updateTrackList(newTrackList: TrackResults?) {
        notifyItemRangeRemoved(0, trackList.size)
        trackList = newTrackList?.trackList ?: listOf()
        notifyItemRangeInserted(0, trackList.size)
    }

    override fun getItemCount() = trackList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.track_list_item, parent, false)
        return ViewHolder(view, onTrackClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    class ViewHolder(
        itemView: View,
        onClick: (Track) -> Unit
        ) : RecyclerView.ViewHolder(itemView) {
        private val trackNameTV: TextView = itemView.findViewById(R.id.tv_track_name)
        private lateinit var currentTrack: Track

//        implement these itemViews once I confirm trackname can be displayed
//        private val artistNameTV: TextView = itemView.findViewById(R.id.tv_artist_name)
//        private val albumNameTV: TextView = itemView.findViewById(R.id.tv_album_name)
//        private val trackIV: ImageView = itemView.findViewById(R.id.iv_track_image)



        init {
            itemView.setOnClickListener {
                currentTrack?.let(onClick)
            }
        }

        fun bind(track: Track) {
            currentTrack = track

            trackNameTV.text = track.track_name
//            artistNameTV.text = track.artist
//            albumNameTV.text = track.album

//            Glide.with(itemView.context)
//                .load(track.imageUrl)
//                .into(trackIV)
        }
    }
}