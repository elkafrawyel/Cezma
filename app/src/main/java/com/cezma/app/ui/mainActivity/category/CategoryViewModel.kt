package com.cezma.app.ui.mainActivity.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.CategoryModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CategoryViewModel : AppViewModel() {
    private var job: Job? = null
    private fun getCategoriesRepo() = Injector.getCategoriesRepo()

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var categoriesList: ArrayList<CategoryModel> = arrayListOf()

    init {
        getCategories()
    }

    private fun getCategories() {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            job = launchJob()
        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    private fun launchJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiState.value = ViewState.Loading }
            when (val result = getCategoriesRepo().getCategories()) {
                is DataResource.Success -> {
                    if (result.data.categories.isNotEmpty()) {
                        categoriesList.clear()
                        categoriesList.addAll(result.data.categories)
                        runOnMainThread {
                            _uiState.value = ViewState.Success
                        }
                    } else {
                        runOnMainThread {
                            _uiState.value = ViewState.Empty
                        }
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiState.value = ViewState.Error(result.message)
                    }
                }
            }
        }
    }

    fun refresh() {
        getCategories()
    }
}
