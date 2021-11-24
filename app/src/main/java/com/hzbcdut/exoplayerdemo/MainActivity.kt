package com.hzbcdut.exoplayerdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.hzbcdut.exoplayerdemo.playonevideo.ExoPlayerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.buttonPlayOneVideo).setOnClickListener {
            ExoPlayerActivity.start(this, "")
        }

        findViewById<View>(R.id.buttonPlayMoreVideo).setOnClickListener {
           startActivity(Intent(this, PlayContactActivity::class.java))
        }
    }
}