package com.nemd.bron

import android.app.Application
import timber.log.Timber

class MeDBroNApplication : Application() {

    override fun onCreate() {
        Timber.d("OnCreate")
        super.onCreate()

        setupLogging()
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("App started!")
        }
    }
}