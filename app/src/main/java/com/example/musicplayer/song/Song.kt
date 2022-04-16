package com.example.musicplayer.song

import android.graphics.drawable.Drawable

data class Song(
    var title: String = "No Title",
    var artist: String = "No Artist",
    var year: String = "",
    var album: String = "",
    var bitRate: String = "",
    var cover: Drawable?
)
