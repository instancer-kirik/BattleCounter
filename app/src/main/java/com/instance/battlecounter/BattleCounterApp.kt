package com.instance.battlecounter

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BattleCounterApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}