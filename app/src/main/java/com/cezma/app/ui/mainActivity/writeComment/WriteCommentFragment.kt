package com.cezma.app.ui.mainActivity.writeComment

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cezma.app.R
import com.cezma.app.data.model.WriteCommentBody
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBar
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.write_comment_fragment.*

class WriteCommentFragment : Fragment() {

    companion object {
        fun newInstance() = WriteCommentFragment()
    }

    private lateinit var viewModel: WriteCommentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.write_comment_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(WriteCommentViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onCommentResponse(it) })
        arguments?.let {
            viewModel.adId = WriteCommentFragmentArgs.fromBundle(it).adId
        }

        if (viewModel.adId == null)
            findNavController().navigateUp()

        confirmBtn.setOnClickListener { confirmReview() }
    }

    private fun onCommentResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                activity?.toast(viewModel.commentMessage)
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.toast(
                    getString(R.string.noConnection)
                )
            }
            ViewState.Empty -> {

            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }
        }
    }

    private fun confirmReview() {
        when {
            writeReviewTiEt.text!!.isBlank() -> activity?.toast(getString(R.string.label_write_review_message))
            rateBar.rating.toInt() < 1 -> activity?.toast(getString(R.string.error_star_count_zero))
            else -> {
                viewModel.writeCommentBody = WriteCommentBody(
                    viewModel.adId!!,
                    writeReviewTiEt.text.toString(),
                    rateBar.rating.toInt()
                )
                viewModel.writeComment()
            }
        }
    }

}
