package com.cezma.app.ui.mainActivity.home.sub_home_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.cezma.app.R
import com.cezma.app.data.model.Ad
import com.cezma.app.data.model.AdImagesToSliderModel
import com.cezma.app.data.model.CategoryModel
import com.cezma.app.ui.adapters.AdapterAds
import com.cezma.app.ui.adapters.ImageSliderAdapter
import com.cezma.app.ui.mainActivity.MainActivity
import com.cezma.app.ui.mainActivity.home.MainHomeFragmentDirections
import com.cezma.app.utiles.*
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.sub_home_fragment.*
import java.util.*
import kotlin.concurrent.timerTask


class SubHomeFragment : Fragment(), BaseQuickAdapter.OnItemChildClickListener {

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        when (view?.id) {
            R.id.adItem -> {
                val adId = (adapter?.data as List<Ad>)[position].adId
                val action =
                    MainHomeFragmentDirections.actionMainHomeFragmentToProductDetailsFragment(adId.toString())
                activity?.findNavController(R.id.fragment)!!.navigate(action)
            }
        }
    }

    companion object {
        fun newInstance() = SubHomeFragment()
    }

    private lateinit var viewModel: SubHomeFragmentViewModel
    private var timer: Timer? = null
    private val imageSliderAdapter = ImageSliderAdapter { position ->
        val images = viewModel.slidersList
        if (images.isNotEmpty()) {
            val action =
                MainHomeFragmentDirections.actionMainHomeFragmentToFullScreenSliderFragment(
                    position,
                    AdImagesToSliderModel(images)
                )
            activity?.findNavController(R.id.fragment)!!.navigate(action)
        }
    }

    private val adsAdapter = AdapterAds().also {
        it.onItemChildClickListener = this
        it.setEnableLoadMore(true)
        it.setOnLoadMoreListener({ viewModel.getAds(true) }, adsRv)
        it.setLoadMoreView(CustomLoadMoreView())
    }

    private val categoriesAdapter = AdapterCategories().also {
        it.onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                when (view?.id) {
                    R.id.categoryName -> {
                        val categoryName =
                            (adapter?.data as List<CategoryModel>)[position].categoryName
                        val categorySlug =
                            (adapter.data as List<CategoryModel>)[position].categorySlug
                        val action =
                            MainHomeFragmentDirections.actionMainHomeFragmentToSubCategoryFragment(
                                categoryName!!, categorySlug!!
                            )
                        activity?.findNavController(R.id.fragment)!!.navigate(action)
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubHomeFragmentViewModel::class.java)

        categoriesRv.setHasFixedSize(true)
        categoriesRv.adapter = categoriesAdapter

        val spacesItemDecoration = SpacesItemDecoration(8)

        if (viewModel.isList) {
            listImgv.setImageDrawable(resources.getDrawable(R.drawable.list))
            gridImgv.setImageDrawable(resources.getDrawable(R.drawable.grid_not_selected))
            adsRv.layoutManager = GridLayoutManager(
                requireContext(),
                1,
                RecyclerView.VERTICAL,
                false
            )
        } else {
            listImgv.background = resources.getDrawable(R.drawable.list_not_selected)
            gridImgv.background = resources.getDrawable(R.drawable.grid)

            adsRv.layoutManager = GridLayoutManager(
                requireContext(),
                2,
                RecyclerView.VERTICAL,
                false
            )
            adsRv.addItemDecoration(spacesItemDecoration)
        }

        listImgv.setOnClickListener {
            if (!viewModel.isList) {
                listImgv.setImageDrawable(resources.getDrawable(R.drawable.list))
                gridImgv.setImageDrawable(resources.getDrawable(R.drawable.grid_not_selected))
                adsRv.post {
                    TransitionManager.beginDelayedTransition(adsRv)
                    (adsRv.layoutManager as GridLayoutManager).spanCount = 1
                    adsRv.removeItemDecoration(spacesItemDecoration)
                }
                viewModel.isList = true
            }
        }

        gridImgv.setOnClickListener {
            if (viewModel.isList) {
                listImgv.setImageDrawable(resources.getDrawable(R.drawable.list_not_selected))
                gridImgv.setImageDrawable(resources.getDrawable(R.drawable.grid))
                adsRv.post {
                    TransitionManager.beginDelayedTransition(adsRv)
                    (adsRv.layoutManager as GridLayoutManager).spanCount = 2
                    adsRv.addItemDecoration(spacesItemDecoration)
                }
                viewModel.isList = false
            }
        }


        adsRv.adapter = adsAdapter
        mScrollView.viewTreeObserver.addOnScrollChangedListener {
            if (mScrollView != null) {
                val view = mScrollView.getChildAt(mScrollView.childCount - 1) as View

                val diff = view.bottom - (mScrollView.height + mScrollView
                    .scrollY)

                if (diff == 0) {
                    viewModel.getAds(true)
                }
            }
        }

        ViewCompat.setNestedScrollingEnabled(adsRv, false)

        if (viewModel.categoriesList.isEmpty()) {
            viewModel.uiState.observe(this, androidx.lifecycle.Observer { onAdsResponse(it) })
            viewModel.uiStateCategory.observe(
                this,
                androidx.lifecycle.Observer { onCategoriesResponse(it) })

            viewModel.getCategories()
        } else {
            loading.visibility = View.GONE
            adsAdapter.replaceData(viewModel.allAds)
            imageSliderAdapter.submitList(viewModel.slidersList)
            bannerSliderVp.adapter = imageSliderAdapter
        }


        addProductFab.setOnClickListener {
            Navigation.findNavController(activity as MainActivity, R.id.fragment)
                .navigate(R.id.action_mainHomeFragment_to_addProductFragment)
        }

        seeAllTv.setOnClickListener {
            val action =
                MainHomeFragmentDirections.actionMainHomeFragmentToCategoryFragment()
            activity?.findNavController(R.id.fragment)!!.navigate(action)
        }

        filterTv.setOnClickListener {
            openFilterDialog()
        }

    }

    private fun onCategoriesResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                dataCl.visibility = View.VISIBLE
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                imageSliderAdapter.submitList(viewModel.slidersList)
                bannerSliderVp.adapter = imageSliderAdapter
                categoriesAdapter.replaceData(viewModel.categoriesList)
                viewModel.getAds()
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView,
                    false
                ) {
                    viewModel.getCategories()
                }
            }
            ViewState.Empty -> {
                loading.visibility = View.GONE
                adsRv.visibility = View.GONE
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                activity?.toast(it.message)
            }
            ViewState.LastPage -> {
                loading.visibility = View.GONE
                adsAdapter.loadMoreEnd()
            }
            null -> {

            }
        }
    }


    private fun openFilterDialog() {
        val filters: Array<String> = arrayOf(
            context!!.getString(R.string.featured),
            context!!.getString(R.string.newFilter),
            context!!.getString(R.string.used),
            context!!.getString(R.string.high_price),
            context!!.getString(R.string.low_price)
        )

        val mBuilder = AlertDialog.Builder(context!!)
        mBuilder.setTitle(getString(R.string.choose_filter))
        mBuilder.setSingleChoiceItems(filters, viewModel.filterIndex) { dialogInterface, i ->
            filterTv.text = filters[i]
            when (i) {
                0 -> {
                    viewModel.isFeatured = 1
                    viewModel.isUsed = null
                    viewModel.priceLevel = null
                }

                1 -> {
                    viewModel.isUsed = 0
                    viewModel.isFeatured = null
                    viewModel.priceLevel = null
                }

                2 -> {
                    viewModel.isUsed = 1
                    viewModel.isFeatured = null
                    viewModel.priceLevel = null
                }

                3 -> {
                    viewModel.isUsed = null
                    viewModel.isFeatured = null
                    viewModel.priceLevel = "high"
                }

                4 -> {
                    viewModel.isUsed = null
                    viewModel.isFeatured = null
                    viewModel.priceLevel = "low"
                }
            }
            viewModel.allAds.clear()
            viewModel.adsList.clear()
            adsAdapter.data.clear()
            adsAdapter.notifyDataSetChanged()
            viewModel.page = 0
            viewModel.lastPage = 1
            viewModel.getAds()
            viewModel.filterIndex = i
            dialogInterface.dismiss()
        }

        // Set the neutral/cancel button click listener
        mBuilder.setNeutralButton(getString(R.string.cancel)) { dialog, which ->
            // Do something when click the neutral button
            dialog.cancel()
        }

        val mDialog = mBuilder.create()
        mDialog.show()
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            requireActivity().runOnUiThread {
                if (bannerSliderVp != null) {
                    if (bannerSliderVp.currentItem < imageSliderAdapter.count - 1) {
                        bannerSliderVp.setCurrentItem(bannerSliderVp.currentItem + 1, true)
                    } else {
                        bannerSliderVp.setCurrentItem(0, true)
                    }
                }
            }
        }, 5000, 5000)
    }

    override fun onPause() {
        timer?.cancel()
        super.onPause()
    }

    private fun onAdsResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
                dataCl.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                adsAdapter.addData(viewModel.adsList)
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView,
                    false
                ) {
                    viewModel.getCategories()
                }
            }
            ViewState.Empty -> {
                adsAdapter.loadMoreFail()
                loading.visibility = View.GONE
                adsRv.visibility = View.GONE
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                activity?.toast(it.message)
            }
            ViewState.LastPage -> {
                loading.visibility = View.GONE
                adsAdapter.loadMoreEnd()
            }
            null -> {

            }
        }
    }
}
