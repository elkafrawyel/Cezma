package com.cezma.app.ui.mainActivity.auth.signUp

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.data.model.CityModel
import com.cezma.app.data.model.CountryModel
import com.cezma.app.data.model.RegisterBody
import com.cezma.app.data.model.StateModel
import com.cezma.app.ui.mainActivity.adComments.AdCommentsFragmentArgs
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.sign_up_fragment.*



class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.cezma.app.R.layout.sign_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onSpinnerDataResponse(it) })
        viewModel.registerLiveData.observe(this, Observer { onRegisterResponse(it) })
        viewModel.getSpinnersData()

        signUpMbtn.setOnClickListener {
            register()
        }

        arguments?.let {
            FirstNameEt.setText(it.getString("firstName"))
            LastNameEt.setText(it.getString("lastName"))
            EmailEt.setText(it.getString("email"))
        }
//        val bundle = this.arguments

    }

    private fun onRegisterResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
                dataCl.visibility = View.GONE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                activity?.toast(viewModel.registerSuccessMessage)
                findNavController().navigate(R.id.loginFragment)
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                activity?.snackBarWithAction(
                    getString(com.cezma.app.R.string.noConnection),
                    getString(com.cezma.app.R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
            null -> {

            }
        }
    }

    private fun onSpinnerDataResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
                dataCl.visibility = View.GONE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.VISIBLE
                setData()
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.GONE
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                dataCl.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(com.cezma.app.R.string.noConnection),
                    getString(com.cezma.app.R.string.retry),
                    rootView
                ) {
                    viewModel.refresh()
                }
            }
            null -> {

            }
        }
    }

    private fun setData() {

        val countriesAdapter =
            ArrayAdapter<CountryModel>(
                context!!,
                android.R.layout.simple_spinner_item,
                viewModel.countries
            )
        countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = countriesAdapter

        if (viewModel.userCountry != null)
            countrySpinner.setSelection(viewModel.countries.indexOf(viewModel.userCountry!!))

        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (viewModel.userCountry!!.id != viewModel.countries[position].id) {
                    viewModel.userCountry = viewModel.countries[position]
                    viewModel.getSpinnersData()
                }
            }
        }

        val statesAdapter =
            ArrayAdapter<StateModel>(
                context!!,
                android.R.layout.simple_spinner_item,
                viewModel.states
            )

        statesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        stateSpinner.adapter = statesAdapter
        if (viewModel.userState != null)
            stateSpinner.setSelection(viewModel.states.indexOf(viewModel.userState!!))

        stateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (viewModel.userState!!.id != viewModel.states[position].id) {
                    viewModel.userState = viewModel.states[position]
                    viewModel.getSpinnersData()
                }
            }
        }

        val citiesAdapter =
            ArrayAdapter<CityModel>(
                context!!,
                android.R.layout.simple_spinner_item,
                viewModel.cities
            )
        citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = citiesAdapter
        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (viewModel.userCity!!.id != viewModel.cities[position].id) {
                    viewModel.userCity = viewModel.cities[position]
                }
            }
        }

        radioMale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.userGender = 1
            else
                viewModel.userGender = 0
        }

        radioFemale.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                viewModel.userGender = 0
            else
                viewModel.userGender = 1
        }

    }

    private fun register() {
        if (FirstNameEt.text.toString().isEmpty()) {
            FirstNameEt.error = resources.getString(com.cezma.app.R.string.emptyField)
            return
        }

        if (LastNameEt.text.toString().isEmpty()) {
            LastNameEt.error = resources.getString(com.cezma.app.R.string.emptyField)
            return
        }


        if (EmailEt.text.toString().isEmpty()) {
            EmailEt.error = resources.getString(com.cezma.app.R.string.emptyField)
            return
        }


        if (phone.text.toString().isEmpty()) {
            phone.error = resources.getString(com.cezma.app.R.string.emptyField)
            return
        }

        if (passwordEt.text.toString().isEmpty()) {
            passwordEt.error = resources.getString(com.cezma.app.R.string.emptyField)
            return
        }

        if (confirmPasswordEt.text.toString().isEmpty()) {
            confirmPasswordEt.error = resources.getString(com.cezma.app.R.string.emptyField)
            return
        }


        val registerBody = RegisterBody(
            first_name = FirstNameEt.text.toString(),
            last_name = LastNameEt.text.toString(),
            username = "${FirstNameEt.text} ${LastNameEt.text}",
            email = EmailEt.text.toString(),
            phone = phone.text.toString(),
            gender = viewModel.userGender.toString(),
            password = passwordEt.text.toString(),
            password_confirmation = confirmPasswordEt.text.toString(),
            city = viewModel.userCity!!.id.toString()
        )
        viewModel.register(registerBody)
    }

}
