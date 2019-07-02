package com.cezma.store

import android.app.Application
import com.blankj.utilcode.util.Utils
import timber.log.Timber

class CezmaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Utils.init(this)

    }

    companion object {
        lateinit var instance: CezmaApp
            private set
    }

}