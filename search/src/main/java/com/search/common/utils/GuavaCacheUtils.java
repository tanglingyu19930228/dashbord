package com.search.common.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class GuavaCacheUtils {

    public static LoadingCache<String, Object> cache = null;

    static {
        cache = getCache();
    }

    private static LoadingCache<String, Object> getCache() {
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(30 * 60, TimeUnit.SECONDS)
                .recordStats()
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return null;
                    }
                });
        return cache;
    }
}
