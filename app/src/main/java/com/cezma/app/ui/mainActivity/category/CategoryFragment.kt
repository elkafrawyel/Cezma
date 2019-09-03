package com.cezma.app.ui.mainActivity.category

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.data.model.CategoryModel
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.category_fragment.*

class CategoryFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    companion object {
        fun newInstance() = CategoryFragment()
    }

    private lateinit var viewModel: CategoryViewModel
    private val adapterCategories = AdapterCategories().also {
        it.onItemChildClickListener = this
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onCategoriesResponse(it) })

        categoryBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        categoriesRv.adapter = adapterCategories
        categoriesRv.setHasFixedSize(true)

    }

    private fun onCategoriesResponse(it: ViewState) {
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
        categoriesRv.visibility = View.GONE

    }

    private fun onSuccess() {
        loading.visibility = View.GONE
        emptyView.visibility = View.GONE
        categoriesRv.visibility = View.VISIBLE

        adapterCategories.replaceData(viewModel.categoriesList)
    }

    private fun onError(message: String) {
        loading.visibility = View.GONE
        emptyView.visibility = View.GONE
        categoriesRv.visibility = View.GONE
        activity?.snackBarWithAction(message, getString(R.string.retry), rootView) {
            viewModel.refresh()
        }
    }

    private fun onEmpty() {
        loading.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
        emptyView.text = getString(R.string.emptyList)
        categoriesRv.visibility = View.GONE
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.categoryItem -> {
                val categoty = (adapter?.data as ArrayList<CategoryModel>)[position]
                val categoryName = categoty.categoryName!!
                val categoryNameApi = categoty.categorySlug!!
                openSubCategoryFragment(categoryName, categoryNameApi)
            }
        }
    }

    private fun openSubCategoryFragment(categoryName: String,categoryNameApi: String) {
        val action =
            CategoryFragmentDirections.actionCategoryFragmentToSubCategoryFragment(categoryName,categoryNameApi)
        findNavController().navigate(action)
    }

}
