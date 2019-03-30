package com.nemd.bron

import android.app.Application
import com.google.firebase.FirebaseApp
import timber.log.Timber

class MeDBroNApplication : Application() {

    override fun onCreate() {
        Timber.d("OnCreate")
        super.onCreate()

        setupLogging()

        FirebaseApp.initializeApp(this)
    }

    private fun setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("App started!")
        }
    }
}