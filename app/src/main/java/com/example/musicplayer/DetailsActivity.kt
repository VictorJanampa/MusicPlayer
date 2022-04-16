package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityDetailsBinding
import com.example.musicplayer.song.Song

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        var songDetails: Song? = Song(cover = null)

        intent?.extras?.let {
            songDetails = Song(
                it.getString("key_title")?:"-",
                it.getString("key_artist")?:"-",
                it.getString("key_album")?:"-",
                it.getString("key_year")?:"-",
                it.getString("key_genre")?:"-",
                it.getString("key_bit_rate")?:"-",
                null
            )
        }

        binding.songDetails = songDetails
    }
}