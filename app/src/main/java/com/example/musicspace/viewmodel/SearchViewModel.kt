package com.example.musicspace.viewmodel

import androidx.lifecycle.ViewModel
import com.example.musicspace.R
import com.example.musicspace.model.Song
import com.example.musicspace.model.SongModel

class SearchViewModel : ViewModel() {
    private val playlistResult = ArrayList<SongModel>()
    private fun getDrawableIdForSong(songId: String): Int {
        return when (songId) {
            "song1" -> R.drawable.song1_img
            "song2" -> R.drawable.song2_img
            "song3" -> R.drawable.song3_img
            "song4" -> R.drawable.song4_img
            "song5" -> R.drawable.song5_img
            "song6" -> R.drawable.song6_img
            else -> R.drawable.song  // A default image if no match is found
        }
    }

    fun queryResult(query: String) {
        playlistResult.clear()
        for (song in getAvailableSongs()) {
            if (song.title.contains(query.toString())) {
                playlistResult.add(SongModel(song.title, song.imageResId))
            }
        }
    }

    private fun getAvailableSongs(): List<Song> {
        // Example of manually creating a list of songs
        return listOf(
            Song("song1", "GO Back", R.drawable.song1_img),
            Song("song2", "Chart Music", R.drawable.song2_img),
            Song("song3", "Haiya Ho lyrics", R.drawable.song3_img),
            Song("song4", "Birdcage", R.drawable.song4_img),
            Song("song5", "Rise My Love", R.drawable.song5_img),
            Song("song6", "I Know", R.drawable.song6_img),
        )
    }
}