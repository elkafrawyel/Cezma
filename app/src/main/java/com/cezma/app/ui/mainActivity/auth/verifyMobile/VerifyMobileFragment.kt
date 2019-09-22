package com.cezma.app.ui.mainActivity.auth.verifyMobile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.utiles.Injector
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.restartApplication
import com.cezma.app.utiles.toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.verify_mobile_fragment.*
import java.util.concurrent.TimeUnit

class VerifyMobileFragment : Fragment() {

    companion object {
        fun newInstance() = VerifyMobileFragment()
    }

    private lateinit var viewModel: VerifyMobileViewModel
    private lateinit var mAuth: FirebaseAuth
    val TAG = "fireAuth"
    lateinit var phoneNumber: String
    lateinit var token: String
    lateinit var refresh_token: String
    var codeSendFromFirebase = "123456"
    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.verify_mobile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(VerifyMobileViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onResponse(it) })
        mAuth = FirebaseAuth.getInstance()
        arguments?.let {
            phoneNumber = "+2" + VerifyMobileFragmentArgs.fromBundle(it).phoneNumber
            token = VerifyMobileFragmentArgs.fromBundle(it).token
            refresh_token = VerifyMobileFragmentArgs.fromBundle(it).refreshToken

            sendVerificationCode(phoneNumber)

            phoneNumberMessage.text =
                resources.getString(R.string.verification_code_has_been_sent_to_you_on_your_mobile_number) + ": $phoneNumber"
        }

        submitCode.setOnClickListener {

            var b = true

            if (TextUtils.isEmpty(verifyMobileEt.text)) {
                b = false
                verifyMobileEt.error = resources.getString(R.string.enter_code)
            } else if (verifyMobileEt.text.toString().length < 6) {
                b = false
                verifyMobileEt.error = resources.getString(R.string.invalid_code)
            }

            if (b) {
                verifySignInCode(verifyMobileEt.text.toString())
                loading.visibility = View.VISIBLE
            }
        }

        resentCode.setOnClickListener {
            sendVerificationCode(phoneNumber)
            resentCode.isEnabled = false
        }


    }

    private fun onResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {

            }
            ViewState.Success -> {
                Injector.getPreferenceHelper().isLoggedIn = true
                Injector.getPreferenceHelper().token = token
                Injector.getPreferenceHelper().refreshToken = refresh_token

                activity?.restartApplication()
            }
            is ViewState.Error -> {

            }
            ViewState.NoConnection -> {

            }
            ViewState.Empty -> {

            }
            ViewState.LastPage -> {

            }
            null -> {

            }
        }
    }

    private fun startTimer() {

        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        countDownTimer = object : CountDownTimer(59000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                resentCode.text =
                    resources.getString(R.string.resend_in) + (millisUntilFinished / 1000).toString() + " " + resources.getString(
                        R.string.second
                    )
                //here you can have your logic to set text to ediText
            }

            override fun onFinish() {
                resentCode.text = resources.getString(R.string.resend)
                resentCode.isEnabled = true
            }

        }.start()
    }

    override fun onPause() {
        super.onPause()
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        startTimer()
        activity?.let {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60, // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                it, // Activity (for callback binding)
                callbacks
            )
        } // OnVerificationStateChangedCallbacks
        // OnVerificationStateChangedCallbacks
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.e(TAG, "onVerificationCompleted:$credential")
            val code = credential.smsCode
            code?.let {

                verifyMobileEt.setText(code)
                verifySignInCode(code)
                loading.visibility = View.VISIBLE
            }

        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.e(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
            }

            // Show a message and update the UI
            // ...
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.e(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them latevr
            codeSendFromFirebase = verificationId

            // ..."
        }
    }


    private fun verifySignInCode(userCode: String) {
        try {
            val credential = PhoneAuthProvider.getCredential(codeSendFromFirebase, userCode)
            signInWithPhoneAuthCredential(credential)
        } catch (e: Exception) {
        }

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        activity?.let {
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(TAG, "signInWithCredential:success")

                        val user = task.result?.user
                        viewModel.active()
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.e(TAG, "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            loading.visibility = View.GONE
                            verifyMobileEt.error = resources.getString(R.string.invalid_code)
                        }
                    }
                }
        }
    }

}
