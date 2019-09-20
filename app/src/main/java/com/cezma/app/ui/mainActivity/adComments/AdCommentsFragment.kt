package com.cezma.app.ui.mainActivity.adComments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cezma.app.R
import com.cezma.app.utiles.CustomLoadMoreView
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBar
import com.cezma.app.utiles.snackBarWithAction
import kotlinx.android.synthetic.main.ad_comments_fragment.*
import kotlinx.android.synthetic.main.ad_comments_fragment.loading
import kotlinx.android.synthetic.main.ad_comments_fragment.rootView
import org.jetbrains.anko.doAsync

class AdCommentsFragment : Fragment() {

    companion object {
        fun newInstance() = AdCommentsFragment()
    }

    private lateinit var viewModel: AdCommentsViewModel
    private var adapterComments = AdapterComments().also {
        it.setEnableLoadMore(true)
        it.setOnLoadMoreListener({ viewModel.getComments(true) }, commentsRv)
        it.setLoadMoreView(CustomLoadMoreView())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ad_comments_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AdCommentsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onCommentsResponse(it) })

        arguments?.let {
            //            viewModel.adId = AdCommentsFragmentArgs.fromBundle(it).adId
            viewModel.adId = "193687343"
            viewModel.getComments()
        }

        backImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        commentsRv.setHasFixedSize(true)
        commentsRv.adapter = adapterComments
    }

    private fun onCommentsResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                adapterComments.addData(viewModel.comments)
                adapterComments.loadMoreComplete()
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
            ViewState.Empty -> {
                loading.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
                emptyView.text = resources.getString(R.string.emptyFavouriteList)
            }

            is ViewState.Error -> {
                adapterComments.loadMoreFail()
                commentsRv.visibility = View.GONE
                loading.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }
            ViewState.LastPage -> {
                loading.visibility = View.GONE
                try {
                    adapterComments.loadMoreEnd()
                }catch (e:Exception){}
            }
            null -> {

            }
        }
    }
}