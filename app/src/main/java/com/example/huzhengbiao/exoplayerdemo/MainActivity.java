package com.example.huzhengbiao.exoplayerdemo;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    // 连续播放数据源
    ConcatenatingMediaSource concatenatingMediaSource;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initPlayer();

        setListener();

        mButton = findViewById(R.id.btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentPos =  mExoPlayer.getCurrentPosition();
                Toast.makeText(MainActivity.this , " currentPos = " + currentPos, Toast.LENGTH_LONG).show();


                concatenatingMediaSource.removeMediaSource(0);
                concatenatingMediaSource.getSize();

            }
        });
    }

    private void initView() {
        mPlayerView = findViewById(R.id.player_view);
    }

    private String[] urls = new String[]{"https://baobab.kaiyanapp.com/api/v1/playUrl?vid=132872&resourceType=video&editionType=default&source=aliyun&ptl=true",
    "https://baobab.kaiyanapp.com/api/v1/playUrl?vid=132873&resourceType=video&editionType=default&source=aliyun&ptl=true"};

    private void initPlayer() {
        //
        TrackSelection.Factory adaptiveTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
        TrackSelector trackSelector = new DefaultTrackSelector(adaptiveTrackSelectionFactory);
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        mPlayerView.setPlayer(mExoPlayer);


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                this, Util.getUserAgent(this, "yourApplicationName"), new DefaultBandwidthMeter());
        // 设置播放源
//        ExtractorMediaSource.Factory factory = new ExtractorMediaSource.Factory(dataSourceFactory);
//        MediaSource[] mediaSources = new MediaSource[2];
//        mediaSources[0] = factory.createMediaSource(Uri.parse(urls[0]));
//        mediaSources[1] = factory.createMediaSource(Uri.parse(urls[1]));



        // ExoPlayer设置缓存？
        File cacheFile = new File(getExternalCacheDir().getAbsolutePath(), "video");
        SimpleCache simpleCache = new SimpleCache(cacheFile, new LeastRecentlyUsedCacheEvictor(512 * 1024 * 1024)); // 本地最多保存512M, 按照LRU原则删除老数据
        // 设置单个数据源缓存的大小
        CacheDataSourceFactory cachedDataSourceFactory = new CacheDataSourceFactory(simpleCache, dataSourceFactory, 0, 20*1024*1024);
        MediaSource[] mediaSources = new MediaSource[urls.length];
    //    设置可缓存的播放源 代表去播放的媒体资源， 如果有缓存的话会从换存中获取？
        MediaSource videoSource = new ExtractorMediaSource.Factory(cachedDataSourceFactory).createMediaSource(Uri.parse(urls[0]));
        mediaSources[0] = videoSource;
        MediaSource cacheMediaSource = new ExtractorMediaSource.Factory(cachedDataSourceFactory).createMediaSource(Uri.parse(urls[1]));
        mediaSources[1] = cacheMediaSource;


        concatenatingMediaSource = new ConcatenatingMediaSource(mediaSources);
        mExoPlayer.prepare(concatenatingMediaSource);

        mExoPlayer.setPlayWhenReady(true);
    }



    private void setListener() {
        mExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

                Log.d("debug", " --< error  = " + error.getCause().toString());
                // 一些播放的错误信息： 比如没有网络请求权限， 没有联网，
            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mExoPlayer.release();
    }
}
