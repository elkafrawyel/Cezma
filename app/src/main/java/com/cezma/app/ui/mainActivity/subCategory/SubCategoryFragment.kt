package com.cezma.app.ui.mainActivity.subCategory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.data.model.SubCategoryModel
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBar
import com.cezma.app.utiles.snackBarWithAction
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.sub_category_fragment.*

class SubCategoryFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = SubCategoryFragment()
    }

    private lateinit var viewModel: SubCategoryViewModel
    private val adapterSubCategories = AdapterSubCategories().also {
        it.onItemChildClickListener = this

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_category_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubCategoryViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onSubCategoriesResponse(it) })

        if (viewModel.subCategoriesList.isEmpty()) {
            arguments?.let {
                val categoryName = SubCategoryFragmentArgs.fromBundle(it).categoryName
                val categoryNameApi = SubCategoryFragmentArgs.fromBundle(it).categoryNameApi
                viewModel.categoryNameApi = categoryNameApi
                viewModel.categoryName = categoryName
                viewModel.getSubCategories()
            }
        }

        categoryTitle.text = viewModel.categoryName

        subCategoryBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        subCategoriesRv.adapter = adapterSubCategories
        subCategoriesRv.setHasFixedSize(true)
    }

    private fun onSubCategoriesResponse(it: ViewState) {
        when (it) {
            ViewState.Loading -> {
                onLoading()
            }
            ViewState.Success -> {
                onSuccess()
            }
            ViewState.NoConnection -> {
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
            ViewState.Empty -> {
                onEmpty()
            }
            is ViewState.Error -> {
                onError(it.message)
            }
        }
    }

    private fun onLoading() {
        loading.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
        subCategoriesRv.visibility = View.GONE

    }

    private fun onSuccess() {
        loading.visibility = View.GONE
        emptyView.visibility = View.GONE
        subCategoriesRv.visibility = View.VISIBLE

        adapterSubCategories.replaceData(viewModel.subCategoriesList)
    }

    private fun onError(message: String) {
        loading.visibility = View.GONE
        emptyView.visibility = View.GONE
        subCategoriesRv.visibility = View.GONE
        activity?.snackBar(message, rootView)
    }

    private fun onEmpty() {
        loading.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.text = getString(R.string.emptyList)
        subCategoriesRv.visibility = View.GONE
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.categoryItem -> {
                val subCategory = (adapter?.data as ArrayList<SubCategoryModel>)[position]
                val subCategoryName = subCategory.categoryName!!
                val subCategoryNameApi = subCategory.categorySlug!!
                openProductsFragment(subCategoryName, subCategoryNameApi)
            }
        }
    }

    private fun openProductsFragment(subCategoryName: String, subCategoryNameApi: String) {
        val action =
            SubCategoryFragmentDirections.actionSubCategoryFragmentToProductsFragment(
                viewModel.categoryNameApi, subCategoryName,subCategoryNameApi
            )
        findNavController().navigate(action)
    }
}
