package com.example.musicspace.viewmodel

import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicspace.model.PlaySong

class PlayPauseSkipViewModel : ViewModel(), MediaPlayer.OnCompletionListener {
    private val _currentSong = MutableLiveData<PlaySong>()
    val currentSong: LiveData<PlaySong> = _currentSong
    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    private var mediaPlayer: MediaPlayer? = null
    private var songUris = mutableListOf<Uri>()
    private var currentSongIndex = 0

    init {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnCompletionListener(this)
    }

    fun initialize(songUris: List<Uri>) {
        this.songUris = songUris.toMutableList()
        if (songUris.isNotEmpty()) {
            currentSongIndex = 0
            playSong(currentSongIndex)
        }
    }

    override fun onCompletion(mp: MediaPlayer) {
        skipToNextSong()
    }

    fun playSong(index: Int) {
        mediaPlayer?.reset()
        val uri = songUris.getOrNull(index)
        uri?.let {
            try {
                mediaPlayer?.setDataSource(it.toString())
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                _isPlaying.postValue(true)
            } catch (e: Exception) {
                Log.e("PlayPauseSkipViewModel", "Error playing song: ${e.message}")
            }
        }
    }

    fun updateCurrentSong(song: PlaySong) {
        _currentSong.postValue(song)
    }

    fun pauseSong() {
        mediaPlayer?.pause()
        _isPlaying.postValue(false)
    }

    fun resumeSong() {
        mediaPlayer?.start()
        _isPlaying.postValue(true)
    }

    fun skipToNextSong() {
        if (songUris.isNotEmpty()) {
            currentSongIndex = (currentSongIndex + 1) % songUris.size
            playSong(currentSongIndex)
        }
    }

    fun skipToPreviousSong() {
        if (songUris.isNotEmpty()) {
            currentSongIndex = if (currentSongIndex > 0) currentSongIndex - 1 else songUris.size - 1
            playSong(currentSongIndex)
        }
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    override fun onCleared() {
        mediaPlayer?.release()
        super.onCleared()
    }
}
