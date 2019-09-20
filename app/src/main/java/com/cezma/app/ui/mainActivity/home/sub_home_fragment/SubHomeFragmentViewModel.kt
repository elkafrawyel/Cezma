package com.cezma.app.ui.mainActivity.home.sub_home_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.Ad
import com.cezma.app.data.model.CategoryModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubHomeFragmentViewModel : AppViewModel() {

    var isList = false
    var isFeatured: Int? = null
    var isUsed: Int? = null
    var priceLevel: String? = null
    var filterIndex = -1
    var page: Int = 0
    var lastPage: Int = 1

    private var job: Job? = null

    private var _uiState = MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    var adsList: ArrayList<Ad> = arrayListOf()
    var allAds: ArrayList<Ad> = arrayListOf()

    fun getAds(loadMore: Boolean = false) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return
            page++
            if (page <= lastPage) {
                job = launchJob(loadMore)
            } else {
                _uiState.value = ViewState.LastPage
            }

        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    private fun launchJob(loadMore: Boolean): Job {
        return scope.launch(dispatcherProvider.io) {
            if (!loadMore) {
                runOnMainThread { _uiState.value = ViewState.Loading }
            }
            when (val result =
                Injector.getHomeAdsRepo().getAds(isFeatured,isUsed,priceLevel,page)) {
                is DataResource.Success -> {
                    if (result.data.ads.isNotEmpty()) {
                        lastPage = result.data.pages
                        if (result.data.ads.isEmpty()) {
                            _uiState.value = ViewState.Empty
                        } else {
                            adsList.clear()
                            adsList.addAll(result.data.ads)
                            allAds.addAll(result.data.ads)
                        }
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
                        _uiState.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

    //========================== Categories ===============================

    var categoriesList: ArrayList<CategoryModel> = arrayListOf()
    var slidersList: ArrayList<String> = arrayListOf()

    private var categoryJob: Job? = null

    private var _uiStateCategory = MutableLiveData<ViewState>()
    val uiStateCategory: LiveData<ViewState>
        get() = _uiStateCategory

    fun getCategories() {
        if (NetworkUtils.isConnected()) {
            if (categoryJob?.isActive == true)
                return
            categoryJob = launchCategoriesJob()
        } else {
            _uiStateCategory.value = ViewState.NoConnection
        }
    }

    private fun launchCategoriesJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            when (val slidersResult = Injector.getSlidersRepo().get()) {
                is DataResource.Success -> {
                    slidersList.clear()
                    slidersList.addAll(slidersResult.data.sliders)

                }
                is DataResource.Error -> {

                }
            }
            when (val result =
                Injector.getCategoriesRepo().getCategories()) {
                is DataResource.Success -> {
                    if (result.data.categories.isNotEmpty()) {
                        if (result.data.categories.isEmpty()) {
                            _uiStateCategory.value = ViewState.Empty
                        } else {
                            categoriesList.addAll(result.data.categories)
                        }
                        runOnMainThread {
                            _uiStateCategory.value = ViewState.Success
                        }
                    } else {
                        runOnMainThread {
                            _uiStateCategory.value = ViewState.Empty
                        }
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateCategory.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }
}
