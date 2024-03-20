package edu.oregonstate.cs492.assignment4.data

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson


/*
    * This class encapsulates data about a single track fetched from the MusixMatch API's
    * track.search.  It does not directly correspond to the JSON data.  The classes below are used for
    * JSON parsing, and information from them is used by the custom JSON adapter at the bottom of
    * this file to construct this class.
 */
/*
  * example API response to query "http://api.musixmatch.com/ws/1.1/track.search?q_artist=justin bieber&page_size=2&page=1"
  * {
    "message": {
        "header": {
            "status_code": 200,
            "execute_time": 0.015164852142334,
            "available": 550
        },
        "body": {
            "track_list": [
                {
                    "track": {
                        "track_id": 33534599,
                        "track_name": "One Love (Justin Bieber Instrumental Tribute)",
                        "track_name_translation_list": [],
                        "track_rating": 1,
                        "commontrack_id": 13418528,
                        "instrumental": 1,
                        "explicit": 0,
                        "has_lyrics": 0,
                        "has_subtitles": 0,
                        "has_richsync": 0,
                        "num_favourite": 0,
                        "album_id": 15458273,
                        "album_name": "One Love (Instrumental Tribute to Justin Bieber)",
                        "artist_id": 24674505,
                        "artist_name": "Karaoke Believers - Tribute to Justin Bieber",
                        "track_share_url": "https://www.musixmatch.com/lyrics/Karaoke-Believers-Tribute-to-Justin-Bieber/One-Love-Justin-Bieber-Instrumental-Tribute?utm_source=application&utm_campaign=api&utm_medium=",
                        "track_edit_url": "https://www.musixmatch.com/lyrics/Karaoke-Believers-Tribute-to-Justin-Bieber/One-Love-Justin-Bieber-Instrumental-Tribute/edit?utm_source=application&utm_campaign=api&utm_medium=",
                        "restricted": 0,
                        "updated_time": "2013-11-28T16:29:42Z",
                        "primary_genres": {
                            "music_genre_list": [
                                {
                                    "music_genre": {
                                        "music_genre_id": 14,
                                        "music_genre_parent_id": 34,
                                        "music_genre_name": "Pop",
                                        "music_genre_name_extended": "Pop",
                                        "music_genre_vanity": "Pop"
                                    }
                                }
                            ]
                        }
                    }
                },
                {
                    "track": {
                        "track_id": 32184720,
                        "track_name": "#that Power (Originally Performed by Will.I.Am) (Karaoke Version)",
                        "track_name_translation_list": [],
                        "track_rating": 1,
                        "commontrack_id": 12585119,
                        "instrumental": 0,
                        "explicit": 0,
                        "has_lyrics": 0,
                        "has_subtitles": 0,
                        "has_richsync": 0,
                        "num_favourite": 0,
                        "album_id": 15343370,
                        "album_name": "#that Power (Originally Performed by Will.I.Am) [Karaoke Version]",
                        "artist_id": 24458650,
                        "artist_name": "American Karaoke Tunes feat. Justin Bieber",
                        "track_share_url": "https://www.musixmatch.com/lyrics/American-Karaoke-Tunes-feat-Justin-Bieber/that-Power-Originally-Performed-by-Will-I-Am-Karaoke-Version?utm_source=application&utm_campaign=api&utm_medium=",
                        "track_edit_url": "https://www.musixmatch.com/lyrics/American-Karaoke-Tunes-feat-Justin-Bieber/that-Power-Originally-Performed-by-Will-I-Am-Karaoke-Version/edit?utm_source=application&utm_campaign=api&utm_medium=",
                        "restricted": 0,
                        "updated_time": "2013-09-20T04:46:50Z",
                        "primary_genres": {
                            "music_genre_list": [
                                {
                                    "music_genre": {
                                        "music_genre_id": 52,
                                        "music_genre_parent_id": 34,
                                        "music_genre_name": "Karaoke",
                                        "music_genre_name_extended": "Karaoke",
                                        "music_genre_vanity": "Karaoke"
                                    }
                                }
                            ]
                        }
                    }
                }
            ]
        }
    }
}
 */

@JsonClass(generateAdapter = true)
data class Track(
    val track_id: Int,
    val track_name: String,
    val track_rating: Int,
    val commontrack_id: Int,
    val instrumental: Int,
    val explicit: Int,
    val has_lyrics: Int,
    val has_subtitles: Int,
    val has_richsync: Int,
    val num_favourite: Int,
    val album_id: Int,
    val album_name: String,
    val artist_id: Int,
    val artist_name: String,
    val track_share_url: String,
    val track_edit_url: String,
    val restricted: Int,
    val updated_time: String,
)

@JsonClass(generateAdapter = true)
data class PrimaryGenres(
    val music_genre_list: List<MusicGenre>
)

@JsonClass(generateAdapter = true)
data class MusicGenre(
    val music_genre: MusicGenreData
)

@JsonClass(generateAdapter = true)
data class MusicGenreData(
    val music_genre_id: Int,
    val music_genre_parent_id: Int,
    val music_genre_name: String,
    val music_genre_name_extended: String,
    val music_genre_vanity: String
)

@JsonClass(generateAdapter = true)
data class Message(
    val body: TrackBody
)

@JsonClass(generateAdapter = true)
data class TrackBody(
    val track_list: List<TrackListItem>
)
@JsonClass(generateAdapter = true)
class TrackListItem(
    val track: Track
)

class MusicTrackJsonAdapter {
    @FromJson
    fun trackFromJson(track: TrackJson) = Track(
        track_id = track.track_id,
        track_name = track.track_name,
        track_rating = track.track_rating,
        commontrack_id = track.commontrack_id,
        instrumental = track.instrumental,
        explicit = track.explicit,
        has_lyrics = track.has_lyrics,
        has_subtitles = track.has_subtitles,
        has_richsync = track.has_richsync,
        num_favourite = track.num_favourite,
        album_id = track.album_id,
        album_name = track.album_name,
        artist_id = track.artist_id,
        artist_name = track.artist_name,
        track_share_url = track.track_share_url,
        track_edit_url = track.track_edit_url,
        restricted = track.restricted,
        updated_time = track.updated_time,
    )

    @ToJson
    fun trackToJson(track: Track) = TrackJson(
        track_id = track.track_id,
        track_name = track.track_name,
        track_rating = track.track_rating,
        commontrack_id = track.commontrack_id,
        instrumental = track.instrumental,
        explicit = track.explicit,
        has_lyrics = track.has_lyrics,
        has_subtitles = track.has_subtitles,
        has_richsync = track.has_richsync,
        num_favourite = track.num_favourite,
        album_id = track.album_id,
        album_name = track.album_name,
        artist_id = track.artist_id,
        artist_name = track.artist_name,
        track_share_url = track.track_share_url,
        track_edit_url = track.track_edit_url,
        restricted = track.restricted,
        updated_time = track.updated_time
    )
}

@JsonClass(generateAdapter = true)
data class TrackJson(
    val track_id: Int,
    val track_name: String,
    val track_rating: Int,
    val commontrack_id: Int,
    val instrumental: Int,
    val explicit: Int,
    val has_lyrics: Int,
    val has_subtitles: Int,
    val has_richsync: Int,
    val num_favourite: Int,
    val album_id: Int,
    val album_name: String,
    val artist_id: Int,
    val artist_name: String,
    val track_share_url: String,
    val track_edit_url: String,
    val restricted: Int,
    val updated_time: String,
)