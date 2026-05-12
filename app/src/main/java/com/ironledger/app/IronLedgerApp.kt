package com.ironledger.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IronLedgerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase or other tools here if needed
    }
}
