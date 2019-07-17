package com.cezma.store.views.mainActivity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cezma.store.R
import com.cezma.store.utiles.Constants
import com.cezma.store.utiles.Injector
import com.cezma.store.utiles.changeLanguage
import com.cezma.store.utiles.saveLanguage

class MainActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        changeLanguage()
    }
}
