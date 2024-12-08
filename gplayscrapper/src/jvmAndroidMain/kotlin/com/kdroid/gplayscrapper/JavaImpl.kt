package com.kdroid.gplayscrapper

import com.kdroid.gplayscrapper.model.GooglePlayApplicationInfo
import com.kdroid.gplayscrapper.services.getGooglePlayApplicationInfo
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import java.util.concurrent.CompletableFuture

@DelicateCoroutinesApi
fun getGooglePlayApplicationInfoAsync(
    appId: String,
    lang: String = "en",
    country: String = "us"
): CompletableFuture<GooglePlayApplicationInfo> {
    return GlobalScope.future {
        getGooglePlayApplicationInfo(appId, lang, country)
    }
}

/*
  Use like this in Java code :

  String appId = "com.android.chrome";
        // Call kotlin function
        CompletableFuture<GooglePlayApplicationInfo> appInfoFuture = getGooglePlayApplicationInfoAsync(appId);

        appInfoFuture.thenAccept(appInfo -> {
            System.out.println("App Info: " + appInfo.getTitle());
        }).exceptionally(throwable -> {
            System.err.println("Error : " + throwable.getMessage());
            return null;
        });
 */