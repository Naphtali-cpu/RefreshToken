package com.mawinda.refresh_token_sample_app

import android.app.Application
import timber.log.Timber

class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}