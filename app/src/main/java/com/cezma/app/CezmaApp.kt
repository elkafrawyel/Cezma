package com.cezma.app

import android.app.Application
import com.blankj.utilcode.util.Utils

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