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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.song.Song


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var songDetails: Song
    private lateinit var mediaMetadataRetriever: MediaMetadataRetriever

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val songList = listOf(R.raw.dreaming)
        songDetails = retrieveMetadata(songList[0])

        binding.songDetails = songDetails
        binding.albumCoverImageView.setImageDrawable(songDetails.cover)

        val mediaPlayer = MediaPlayer.create(this, songList[0])

        binding.playPauseButton.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                it.background = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_pause_24)
            }else{
                mediaPlayer.pause()
                it.background = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_play_arrow_24)
            }
        }
        binding.skipNextButton.setOnClickListener {

        }
        binding.skipPreviousButton.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.prepare()
            mediaPlayer.start()
            binding.playPauseButton.background = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_pause_24)
        }

    }

        private fun retrieveMetadata(resourceId: Int): Song {
        mediaMetadataRetriever = MediaMetadataRetriever()
        val uri = Uri.parse("android.resource://${this.packageName}/$resourceId")

        mediaMetadataRetriever.setDataSource(this, uri)
        mediaMetadataRetriever.run {
             return Song(
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString(),
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST).toString(),
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR).toString(),
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString(),
                extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).toString(),
                embeddedPicture.toDrawable() ?: AppCompatResources.getDrawable(baseContext, R.drawable.albumart)
            )
        }
    }

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

private fun ByteArray?.toDrawable(): Drawable? {
    return this?.let {
        BitmapDrawable(Resources.getSystem() ,BitmapFactory.decodeByteArray(this, 0, it.size))
    }
}


