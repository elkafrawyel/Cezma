package com.cezma.app.ui.mainActivity.contactUs

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.data.model.ContactUsBody
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.contact_us_fragment.*

class ContactUsFragment : Fragment() {

    companion object {
        fun newInstance() = ContactUsFragment()
    }

    private lateinit var viewModel: ContactUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_us_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ContactUsViewModel::class.java)
        viewModel.uiState.observe(this, Observer { response(it) })
        contactUsBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        contactUsSendMessageMbtn.setOnClickListener{
            contactUs()
        }
    }

    private fun response(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                activity?.toast(viewModel.message)
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.toast(getString(R.string.noConnection))
            }
            null -> {

            }
        }
    }

    private fun contactUs(){
        if (fullNameEt.text.isEmpty()){
            fullNameEt.error = getString(R.string.emptyField)
            return
        }

        if (emailEt.text.isEmpty()){
            emailEt.error = getString(R.string.emptyField)
            return
        }

        if (phoneEt.text.isEmpty()){
            phoneEt.error = getString(R.string.emptyField)
            return
        }

        if (contactUsSubjectEt.text.isEmpty()){
            contactUsSubjectEt.error = getString(R.string.emptyField)
            return
        }

        if (contactUsMessageEt.text.isEmpty()){
            contactUsMessageEt.error = getString(R.string.emptyField)
            return
        }

        val body = ContactUsBody(
            full_name = fullNameEt.text.toString(),
            email = emailEt.text.toString(),
            message = contactUsMessageEt.text.toString(),
            phone = phoneEt.text.toString(),
            subject = contactUsSubjectEt.text.toString()
        )

        viewModel.contactUs(body)
    }

}
