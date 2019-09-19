package com.cezma.app.ui.mainActivity.auth.verifyPhone

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cezma.app.R
import kotlinx.android.synthetic.main.receive_number_fragment.*


class ReceiveNumberFragment : Fragment() {

    companion object {
        fun newInstance() = ReceiveNumberFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.receive_number_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sendCodeMbtn.setOnClickListener {
            var b = true

            if (TextUtils.isEmpty(phoneNumberEt.text)) {
                b = false
                phoneNumberEt.error = resources.getString(R.string.enter_phone)
            } else if (phoneNumberEt.text.toString().length < 11) {
                b = false
                phoneNumberEt.error = resources.getString(R.string.enter_valid_phone)
            }

            if (b) {

                val action =
                    ReceiveNumberFragmentDirections.actionVerifyPhoneNumberToReceiveNumberFragment(phoneNumberEt.text.toString())
                findNavController().navigate(action)
            }

        }


    }
}