package com.cezma.app.utiles

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.cezma.app.R
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun Context.changeLanguage() {
    var locale: Locale? = null
    when (Injector.getPreferenceHelper().language) {
        Constants.Language.ARABIC.value -> {
            locale = Locale("ar")
        }
        Constants.Language.ENGLISH.value -> {
            locale = Locale("en")
        }
    }
    Locale.setDefault(locale)
    val config = this.resources.configuration
    config.setLocale(locale)
    this.createConfigurationContext(config)
//    Injector.getApplicationContext().createConfigurationContext(config)
    this.resources.updateConfiguration(config, this.resources.displayMetrics)
}


fun Context.restartApplication() {
    val intent = Injector.getApplicationContext().packageManager.getLaunchIntentForPackage(
        Injector.getApplicationContext().packageName
    )
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    startActivity(intent)
}

fun Context.saveLanguage(language: Constants.Language) {
    Injector.getPreferenceHelper().language = language.value
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.snackBarWithAction(
    message: String,
    actionTitle: String,
    rootView: View,
    action: () -> Unit
) {
    val snackBar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
    snackBar.setAction(actionTitle) {
        action.invoke()
        snackBar.dismiss()
    }
    snackBar.show()
}

fun Context.snackBar(message: String?, rootView: View) {
    val snackBar = Snackbar.make(rootView, message!!, Snackbar.LENGTH_LONG)
    val view = snackBar.view
    val textView = view.findViewById<View>(R.id.snackbar_text)
    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
    snackBar.show()
}




