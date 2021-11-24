package com.hzbcdut.exoplayerdemo;

import android.content.Context;

import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

/**
 * 主要功能:
 * author huzhengbiao
 * date : On 2018/10/23
 */
//修改设置SimpleCache存储的方式
//        [android \- java\.lang\.IllegalStateException: Another SimpleCache instance uses the folder: \- Stack Overflow](https://stackoverflow.com/questions/52507270/java-lang-illegalstateexception-another-simplecache-instance-uses-the-folder)
public class VideoCache {
    private static SimpleCache sDownloadCache;

    public static SimpleCache getInstance(Context context) {
        if (sDownloadCache == null) sDownloadCache = new SimpleCache(new File(context.getExternalCacheDir().getAbsolutePath(), "video"),
                new NoOpCacheEvictor());
        return sDownloadCache;
    }
}