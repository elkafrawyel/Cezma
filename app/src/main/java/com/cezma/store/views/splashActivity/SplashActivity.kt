package com.cezma.store.views.splashActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cezma.store.R
import com.cezma.store.views.mainActivity.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        MainActivity.start(this)
        finish()
    }
}
