package com.example.huzhengbiao.exoplayerdemo;

import android.content.Context;

import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/23
 */
public class VideoCache {
    private static SimpleCache sDownloadCache;

    public static SimpleCache getInstance(Context context) {
        if (sDownloadCache == null) sDownloadCache = new SimpleCache(new File(context.getExternalCacheDir().getAbsolutePath(), "video"),
                new NoOpCacheEvictor());
        return sDownloadCache;
    }
}