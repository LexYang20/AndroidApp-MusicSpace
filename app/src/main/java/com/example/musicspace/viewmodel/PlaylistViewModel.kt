package com.example.musicspace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicspace.R
import com.example.musicspace.model.Song
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class PlaylistViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val playlist = MutableLiveData<MutableList<Song>>()
    private var currentPlaylistId: String = UUID.randomUUID().toString() // Unique ID for the playlist


    val saveSuccess = MutableLiveData<Boolean>()
    val saveError = MutableLiveData<String>()

    init {
        playlist.value = mutableListOf()
    }

    fun addSongToPlaylist(songId: String, title: String) {
        val imageResId = getDrawableIdForSong(songId)
        val song = Song(songId, title, imageResId)
        val updatedPlaylist = playlist.value ?: mutableListOf()
        updatedPlaylist.add(song)
        playlist.value = updatedPlaylist
    }

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

    fun removeSongFromPlaylist(songId: String) {
        val updatedPlaylist = playlist.value?.filterNot { it.id == songId }?.toMutableList()
        playlist.value = updatedPlaylist
    }


    fun savePlaylistToFirestore() {
        val songIds = playlist.value?.map { it.id } ?: emptyList()
        val playlistData = hashMapOf("songs" to songIds)

        db.collection("playlists").document(currentPlaylistId).set(playlistData)
            .addOnSuccessListener {
                saveSuccess.value = true // Notify that the save was successful
            }
            .addOnFailureListener { e ->
                saveError.value = e.message // Pass the error message to the UI
            }
    }

}
