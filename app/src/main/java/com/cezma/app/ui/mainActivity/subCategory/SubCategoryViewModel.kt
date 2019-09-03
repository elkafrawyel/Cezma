package com.cezma.app.ui.mainActivity.subCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.SubCategoryModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubCategoryViewModel : AppViewModel() {

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var subCategoriesList: ArrayList<SubCategoryModel> = arrayListOf()

    lateinit var categoryName: String
    lateinit var categoryNameApi: String

    fun getSubCategories() {
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
            when (val result = Injector.getSubCategoriesRepo().getSubCategories(categoryNameApi)) {
                is DataResource.Success -> {
                    if (result.data.subCategories != null && result.data.subCategories.isNotEmpty()) {
                        subCategoriesList.clear()
                        subCategoriesList.addAll(result.data.subCategories)
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
        getSubCategories()
    }
}
