package com.hzbcdut.exoplayerdemo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.*
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.util.Util

/**
 * ExoPlayer连续播放
 */
class PlayContactActivity : AppCompatActivity() {
    private var mPlayerView: PlayerView? = null
    private var mExoPlayer: SimpleExoPlayer? = null

    // 连续播放数据源
    var concatenatingMediaSource: ConcatenatingMediaSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_contact)

        initView()
        initPlayer()

        setListener()

        findViewById<View>(R.id.btn).setOnClickListener {
            // 设置ExoPlayer连续播放 点击移除第一条数据
            concatenatingMediaSource?.removeMediaSource(0)
            concatenatingMediaSource?.size
        }
    }


    private fun initView() {
        mPlayerView = findViewById(R.id.player_view)
    }

    private val urls = arrayOf("https://baobab.kaiyanapp.com/api/v1/playUrl?vid=132872&resourceType=video&editionType=default&source=aliyun&ptl=true",
        "https://baobab.kaiyanapp.com/api/v1/playUrl?vid=132873&resourceType=video&editionType=default&source=aliyun&ptl=true")

    private fun initPlayer() {
        //
        val adaptiveTrackSelectionFactory: TrackSelection.Factory = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
        val trackSelector: TrackSelector = DefaultTrackSelector(adaptiveTrackSelectionFactory)
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)
        mPlayerView?.player = mExoPlayer
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            this, Util.getUserAgent(this, "yourApplicationName"), DefaultBandwidthMeter())
        // 设置播放源
//        ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(dataSourceFactory);
//        MediaSource[] mediaSources = new MediaSource[2];
//        mediaSources[0] = factory.createMediaSource(Uri.parse(urls[0]));
//        mediaSources[1] = factory.createMediaSource(Uri.parse(urls[1]));


        // ExoPlayer设置缓存？
//        File cacheFile = new File(getExternalCacheDir().getAbsolutePath(), "video");
//        SimpleCache simpleCache = new SimpleCache(cacheFile, new LeastRecentlyUsedCacheEvictor(512 * 1024 * 1024)); // 本地最多保存512M, 按照LRU原则删除老数据
        // 设置单个数据源缓存的大小
        val cachedDataSourceFactory = CacheDataSourceFactory(VideoCache.getInstance(this), dataSourceFactory, 0, 100 * 1024 * 1024)
        val mediaSources = arrayOfNulls<MediaSource>(urls.size)
        // 设置连续播放的播放源
        //    设置可缓存的播放源 代表去播放的媒体资源， 如果有缓存的话会从换存中获取？
        val videoSource: MediaSource = ExtractorMediaSource.Factory(cachedDataSourceFactory).createMediaSource(Uri.parse(urls[0]))
        mediaSources[0] = videoSource
        val cacheMediaSource: MediaSource = ExtractorMediaSource.Factory(cachedDataSourceFactory).createMediaSource(Uri.parse(urls[1]))
        mediaSources[1] = cacheMediaSource
        concatenatingMediaSource = ConcatenatingMediaSource(*mediaSources)
        mExoPlayer?.prepare(concatenatingMediaSource)
        mExoPlayer?.playWhenReady = true
    }


    private fun setListener() {
        mExoPlayer?.addListener(object : Player.EventListener {
            override fun onTimelineChanged(timeline: Timeline, @Nullable manifest: Any?, reason: Int) {}
            override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {}
            override fun onLoadingChanged(isLoading: Boolean) {}
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {}
            override fun onRepeatModeChanged(repeatMode: Int) {}
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
            override fun onPlayerError(error: ExoPlaybackException) {
                Log.d("debug", " --< error  = " + error.cause.toString())
                // 一些播放的错误信息： 比如没有网络请求权限， 没有联网，
                Toast.makeText(this@PlayContactActivity, "error = " + error.cause.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onPositionDiscontinuity(reason: Int) {}
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
            override fun onSeekProcessed() {}
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        mExoPlayer?.release()
    }
}