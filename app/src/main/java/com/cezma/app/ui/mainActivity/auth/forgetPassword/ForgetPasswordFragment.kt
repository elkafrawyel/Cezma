package com.cezma.app.ui.mainActivity.auth.forgetPassword

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import kotlinx.android.synthetic.main.forget_password_fragment.*

class ForgetPasswordFragment : Fragment() {

    companion object {
        fun newInstance() = ForgetPasswordFragment()
    }

    private lateinit var viewModel: ForgetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forget_password_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForgetPasswordViewModel::class.java)
        backForgotPassword.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
