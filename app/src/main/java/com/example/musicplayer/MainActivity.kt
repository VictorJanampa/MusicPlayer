package com.example.musicplayer

import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.song.Song


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songDetails: Song
    private lateinit var mediaMetadataRetriever: MediaMetadataRetriever
    private lateinit var mediaPlayer: MediaPlayer
    private var currentSong: Int = 0
    private var playlistSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val songList = listOf(R.raw.hard_in_da_paint, R.raw.dreaming, R.raw.dreamer).also { playlistSize = it.size }
        songDetails = retrieveMetadata(songList[currentSong])

        binding.songDetails = songDetails
        binding.albumCoverImageView.setImageDrawable(songDetails.cover)

        mediaPlayer = MediaPlayer.create(this, songList[currentSong])

        binding.playPauseButton.setOnClickListener {
            if (!mediaPlayer.isPlaying) playMusic() else pauseMusic()
        }
        binding.skipNextButton.setOnClickListener {
            currentSong = (currentSong+1) % songList.size
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(this, songList[currentSong]).apply {  }
            playMusic()
            updateUI(songList)
        }
        binding.skipPreviousButton.setOnClickListener {
            currentSong--
            if (currentSong < 0) currentSong = playlistSize - 1
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(this, songList[currentSong])
            updateUI(songList)
            playMusic()
        }
    }
    // functions to control the player
    private fun playMusic() {
        mediaPlayer.start()
        binding.playPauseButton.background = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_pause_24)
    }

    private fun pauseMusic() {
        mediaPlayer.pause()
        binding.playPauseButton.background = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_play_arrow_24)
    }

    // Update UI

    private fun updateUI(songList: List<Int>) {
        songDetails = retrieveMetadata(songList[currentSong])
        binding.albumCoverImageView.setImageDrawable(songDetails.cover)
    }

    // Get Metadata
    private fun retrieveMetadata(resourceId: Int): Song {
        mediaMetadataRetriever = MediaMetadataRetriever()

        val uri = Uri.parse("android.resource://${this.packageName}/$resourceId")

        mediaMetadataRetriever.setDataSource(this, uri)
        val song = mediaMetadataRetriever.run {
             Song(
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString(),
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString(),
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR).toString(),
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString(),
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).toString(),
                embeddedPicture.toDrawable() ?: AppCompatResources.getDrawable(baseContext, R.drawable.albumart)
            )
        }
        return song
    }

    // Options Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.song_detail_menu) {
            startActivity(Intent(this,DetailsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}

// Extension to convert a ByteArray to Drawable
private fun ByteArray?.toDrawable(): Drawable? {
    return this?.let {
        BitmapDrawable(Resources.getSystem() ,BitmapFactory.decodeByteArray(this, 0, it.size))
    }
}


