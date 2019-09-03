package com.cezma.app.ui

import androidx.lifecycle.ViewModel
import com.koraextra.app.utily.CoroutinesDispatcherProvider
import kotlinx.coroutines.*

open class AppViewModel : ViewModel() {

    private val coroutinesDispatcherProvider = CoroutinesDispatcherProvider(
        Dispatchers.Main,
        Dispatchers.Default,
        Dispatchers.IO
    )

    val dispatcherProvider = coroutinesDispatcherProvider

    private val parentJob = Job()
    val scope = CoroutineScope(Dispatchers.Main + parentJob)

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun runOnMainThread(action: () -> Unit) {
        scope.launch {
            withContext(dispatcherProvider.main) {
                action.invoke()
            }
        }
    }
}