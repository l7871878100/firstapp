package com.example.firstapp

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.song)
        mediaPlayer.start()
//        findViewById<Button>(R.id.start).setOnClickListener {
//            mediaPlayer.start()
//        }
//        findViewById<Button>(R.id.pause).setOnClickListener {
//            mediaPlayer.pause()
//        }
//        findViewById<Button>(R.id.stop).setOnClickListener {
//            mediaPlayer.stop()
//        }

    }

    fun onClickPause(view: View) {
        mediaPlayer.pause()

    }

    fun onClickStart(view: View) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.prepare()
        } else
            mediaPlayer.start()
    }

    fun onClickStop(view: View) {
        mediaPlayer.stop()
    }

}
