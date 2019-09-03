package com.cezma.app.ui.splashActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cezma.app.R
import com.cezma.app.ui.mainActivity.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        MainActivity.start(this)
        finish()
    }
}
