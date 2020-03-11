package com.seven.joker.exoplayer;

import android.net.Uri;

import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSinkFactory;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.seven.joker.QiApplication;

import java.util.HashMap;

public class PageListPlayManager {
    private static final ProgressiveMediaSource.Factory mediaSourceFactory;
    private static HashMap<String, PageListPlay> pageListPlayHashMap = new HashMap<>();

    static {
        QiApplication qiApplication = QiApplication.getInstance();
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(qiApplication, qiApplication.getPackageName()));
        Cache cache = new SimpleCache(qiApplication.getCacheDir(), new LeastRecentlyUsedCacheEvictor(1024 * 1024 * 200));
        CacheDataSinkFactory cacheDataSinkFactory = new CacheDataSinkFactory(cache, Long.MAX_VALUE);
        CacheDataSourceFactory cacheDataSourceFactory = new CacheDataSourceFactory(cache, dataSourceFactory, new FileDataSourceFactory(), cacheDataSinkFactory, CacheDataSource.FLAG_BLOCK_ON_CACHE, null);
        mediaSourceFactory = new ProgressiveMediaSource.Factory(cacheDataSourceFactory);
    }



    public static PageListPlay get(String pageName) {
        PageListPlay pageListPlay = pageListPlayHashMap.get(pageName);
        if (pageListPlay == null) {
            pageListPlay = new PageListPlay();
            pageListPlayHashMap.put(pageName, pageListPlay);
        }
        return pageListPlay;
    }

    public static MediaSource creatMediaSource(String url) {
        return mediaSourceFactory.createMediaSource(Uri.parse(url));
    }

    public static void release(String pageName) {
        PageListPlay pageListPlay = pageListPlayHashMap.get(pageName);
        if (pageListPlay != null) {
            pageListPlay.release();
        }
    }
}
