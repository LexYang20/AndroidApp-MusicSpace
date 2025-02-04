package com.example.musicspace.model

class SongModel(private var songName: String, private var songImage: Int) {
    fun getSongName(): String {
        return songName
    }

    fun setSongName(songName: String) {
        this.songName = songName
    }

    fun getSongImage(): Int {
        return songImage
    }

    fun setSongImage(songImage: Int) {
        this.songImage = songImage
    }
}