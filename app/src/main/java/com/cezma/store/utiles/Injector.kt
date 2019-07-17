package com.cezma.store.utiles

import com.cezma.store.CezmaApp
import com.cezma.store.data.storage.local.PreferencesHelper

object Injector {

    fun getApplicationContext() = CezmaApp.instance
    fun getPreferenceHelper() = PreferencesHelper(getApplicationContext())


}