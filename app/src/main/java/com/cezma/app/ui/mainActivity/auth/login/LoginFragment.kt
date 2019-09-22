package com.cezma.app.ui.mainActivity.auth.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.KeyboardUtils

import com.cezma.app.R
import com.cezma.app.data.model.LoginBody
import com.cezma.app.ui.mainActivity.MainActivity
import com.cezma.app.utiles.*
import com.koraextra.app.utily.observeEvent
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.uiState.observeEvent(this) { onLoginResponse(it) }

        singnUpMbtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        forgotPasswordTv.setOnClickListener {
        }

        loginMbtn.setOnClickListener {
            KeyboardUtils.hideSoftInput(activity)
            login()
        }

        facebookMbtn.setOnClickListener {
            (activity as MainActivity).loginWithFacebook()
        }
    }

    private fun onLoginResponse(it: ViewState) {
        when (it) {
            ViewState.Loading -> {
                onLoading()
            }
            ViewState.Success -> {
                onSuccess()
            }
            ViewState.NoConnection -> {
                onNoConnection()
            }
            ViewState.Empty -> {
            }
            is ViewState.Error -> {
                onError(it.message)
            }
        }
    }

    private fun onNoConnection() {
        loading.visibility = View.GONE
        activity?.snackBarWithAction(
            getString(R.string.noConnection),
            getString(R.string.retry),
            rootView
        ) {
            login()
        }
    }

    private fun onError(message: String) {
        loading.visibility = View.GONE
        activity?.snackBar(message, rootView)
    }

    private fun onSuccess() {
        loading.visibility = View.GONE

        activity?.toast(getString(R.string.loginSuccess))
        if (viewModel.phoneVerified) {
            // go to home
            Injector.getPreferenceHelper().isLoggedIn = true
            Injector.getPreferenceHelper().token = viewModel.token
            Injector.getPreferenceHelper().refreshToken = viewModel.refreshToken

            activity?.restartApplication()
        } else {
            if (viewModel.phoneNumber == null)
                findNavController().navigate(R.id.receiveNumberFragment)
            else
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToVerifyMobileFragment(
                        viewModel.phoneNumber!!,
                        viewModel.token!!,
                        viewModel.refreshToken!!
                    )
                )
        }
    }

    private fun onLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun login() {
        if (emailEt.text.isEmpty()) {
            emailEt.error = getString(R.string.emptyEmail)
            return
        }

        if (passwordEt.text.isEmpty()) {
            passwordEt.error = getString(R.string.emptyPassword)
            return
        }

        viewModel.login(LoginBody(emailEt.text.toString(), passwordEt.text.toString()))
    }

}
