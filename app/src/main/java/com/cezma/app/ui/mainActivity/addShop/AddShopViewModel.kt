package com.cezma.app.ui.mainActivity.addShop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.NetworkUtils
import com.cezma.app.data.model.AddShopBody
import com.cezma.app.data.model.CategoryModel
import com.cezma.app.data.model.SubCategoryModel
import com.cezma.app.ui.AppViewModel
import com.cezma.app.utiles.DataResource
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File


class AddShopViewModel : AppViewModel() {

    var categories: ArrayList<CategoryModel> = arrayListOf()
    var subCategories: ArrayList<SubCategoryModel> = arrayListOf()

    var selectedCategory: CategoryModel? = null
    var selectedSubCategory: SubCategoryModel? = null


    private var jobCategories: Job? = null
    private var jobSubCategories: Job? = null

    private var _uiStateCategories = MutableLiveData<ViewState>()
    val uiStateCategories: LiveData<ViewState>
        get() = _uiStateCategories


    private var _uiStateSubCategories = MutableLiveData<ViewState>()
    val uiStateSubCategories: LiveData<ViewState>
        get() = _uiStateSubCategories


    init {
        getCategoriesData()
    }

    private fun getCategoriesData() {
        if (NetworkUtils.isConnected()) {
            if (jobCategories?.isActive == true)
                return
            jobCategories = launchCategoriesJob()
        } else {
            _uiStateCategories.value = ViewState.NoConnection
        }
    }

    private fun launchCategoriesJob(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateCategories.value = ViewState.Loading }

            when (val result = Injector.getCategoriesRepo().getCategories()) {
                is DataResource.Success -> {
                    if (result.data.categories.isNotEmpty()) {
                        categories.clear()
                        categories.addAll(result.data.categories)

                        selectedCategory = categories[0]

                        runOnMainThread {
                            _uiStateCategories.value = ViewState.Success
                        }

                        getSubCategoriesData()
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateCategories.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

    fun getSubCategoriesData() {
        if (NetworkUtils.isConnected()) {
            if (jobSubCategories?.isActive == true)
                return
            jobSubCategories = launchSubCategories()
        } else {
            _uiStateSubCategories.value = ViewState.NoConnection
        }
    }

    private fun launchSubCategories(): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiStateSubCategories.value = ViewState.Loading }

            when (val result =
                Injector.getSubCategoriesRepo()
                    .getSubCategories(selectedCategory!!.categorySlug!!)) {
                is DataResource.Success -> {
                    if (result.data.subCategories.isNotEmpty()) {

                        subCategories.clear()
                        subCategories.addAll(result.data.subCategories)

                        if (subCategories.isNotEmpty()) {
                            selectedSubCategory = subCategories[0]

                            runOnMainThread {
                                _uiStateSubCategories.value = ViewState.Success
                            }
                        } else {
                            _uiStateSubCategories.value = ViewState.Empty
                        }
                    }
                }
                is DataResource.Error -> {
                    runOnMainThread {
                        _uiStateSubCategories.value = ViewState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

    fun refresh() {
        getCategoriesData()
    }

    //======================================= Images =======================================

    var logoPath: String? = null

    var coverPath: String? = null

    fun getLogoImageFile(): File {
        return File(logoPath!!)
    }


    fun getCoverImageFile(): File {
        return File(coverPath!!)
    }

    //====================================== Add Shop ========================================
    private var job: Job? = null

    var addShopMessage: String = ""
    private var _uiState= MutableLiveData<ViewState>()
    val uiState: LiveData<ViewState>
        get() = _uiState

    fun addShop(addShopBody: AddShopBody, logo: File,cover: File) {
        if (NetworkUtils.isConnected()) {
            if (job?.isActive == true)
                return

            launchJob(addShopBody,logo,cover)

        } else {
            _uiState.value = ViewState.NoConnection
        }
    }

    private fun launchJob(
        addShopBody: AddShopBody,
        logo: File,
        cover: File
    ): Job {
        return scope.launch(dispatcherProvider.io) {
            runOnMainThread { _uiState.value = ViewState.Loading }

            when (val result =
                Injector.getAddShopRepo().addShop(addShopBody,logo,cover)) {
                is DataResource.Success -> {
                    addShopMessage = result.data.message
                    runOnMainThread {
                        _uiState.value = ViewState.Success
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
}
