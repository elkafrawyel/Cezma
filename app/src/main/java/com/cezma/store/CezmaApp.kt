package com.cezma.store

import android.app.Application
import com.blankj.utilcode.util.Utils
import com.cezma.store.utiles.changeLanguage
import timber.log.Timber

class CezmaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        Utils.init(this)

    }

    companion object {
        lateinit var instance: CezmaApp
            private set
    }

}