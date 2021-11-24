package com.hzbcdut.exoplayerdemo.playonevideo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hzbcdut.exoplayerdemo.R
import android.content.Intent

class ExoPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exo_player)
        val videoPath = intent.getStringExtra("videoPath")
        supportFragmentManager.beginTransaction().add(R.id.root, ExoPlayerFragment.newInstance(videoPath)).commitAllowingStateLoss()
    }

    companion object {
        fun start(context: Context, videoPath: String?) {
            val intent = Intent(context, ExoPlayerActivity::class.java)
            intent.putExtra("videoPath", videoPath)
            context.startActivity(intent)
        }
    }
}