package com.buildbychris.jikan

import android.app.Application
import com.buildbychris.jikan.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class JikanApplication(): Application() {
    override fun onCreate() {
        super.onCreate()
        //Init koin for android
        initKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@JikanApplication)
        }
    }
}