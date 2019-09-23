package com.cezma.app.ui.mainActivity.profile.editProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
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
import com.bumptech.glide.Glide

import com.cezma.app.R
import com.cezma.app.data.model.CityModel
import com.cezma.app.data.model.CountryModel
import com.cezma.app.data.model.StateModel
import com.cezma.app.data.model.UpdateProfileBody
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.getRealPathFromUri
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.edit_profile_fragment.*
import kotlinx.android.synthetic.main.edit_profile_fragment.stateSpinner
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

const val RC_PERMISSION_STORAGE = 111
const val RC_IMAGES = 112

class EditProfileFragment : Fragment() {

    companion object {
        fun newInstance() = EditProfileFragment()
    }

    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_profile_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)
        viewModel.uiState.observe(this, Observer { onSpinnerDataResponse(it) })
        viewModel.uiStateUpdateProfile.observe(this, Observer { onUpdateProfileResponse(it) })
        arguments?.let {
            val userModel = EditProfileFragmentArgs.fromBundle(it).userModel
            viewModel.userModel = userModel
            viewModel.getSpinnersData()
        }

        editProfileBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        profileSaveImgv.setOnClickListener {
            if (viewModel.userImagePath != null)
                checkStorage()
            else
                updateProfile()
        }

        profileUserImage.setOnClickListener {
            chooseAvatar()
        }
    }

    private fun onUpdateProfileResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                activity?.toast(viewModel.updateMessage!!)
                findNavController().navigateUp()
                findNavController().navigateUp()
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                activity?.toast(it.message)
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    checkStorage()
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
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                setData()
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
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
            null -> {

            }
        }
    }

    private fun setData() {
        val user = viewModel.userModel
        if (user != null) {
            Glide.with(context!!).load(user.avatar).into(profileUserImage)
            firstName.setText(user.firstName)
            lastName.setText(user.lastName)
            code.setText(user.phonecode)
            phone.setText(user.phone)
            email.setText(user.email)
        }
        val countriesAdapter =
            ArrayAdapter<CountryModel>(
                context!!,
                android.R.layout.simple_spinner_item,
                viewModel.countries
            )
        countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        countrySpinner.adapter = countriesAdapter
        countrySpinner.setSelection(viewModel.countries.indexOf(viewModel.userCounty))
        countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (viewModel.userCounty!!.id != viewModel.countries[position].id) {
                    viewModel.userModel!!.country_id = viewModel.countries[position].id
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
        stateSpinner.setSelection(viewModel.states.indexOf(viewModel.userState))
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
                    viewModel.userModel!!.state = viewModel.states[position].id
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
        if (viewModel.userCity != null) {
            citySpinner.setSelection(viewModel.cities.indexOf(viewModel.userCity!!))
        }
    }

    private fun updateProfile() {
        if (firstName.text.toString().isEmpty()) {
            firstName.error = resources.getString(R.string.emptyField)
            return
        } else
            viewModel.userModel!!.firstName = firstName.text.toString()

        if (lastName.text.toString().isEmpty()) {
            lastName.error = resources.getString(R.string.emptyField)
            return
        } else
            viewModel.userModel!!.lastName = lastName.text.toString()

        viewModel.userModel!!.username = ""
        viewModel.userModel!!.username = "${firstName.text} ${lastName.text}"

        if (email.text.toString().isEmpty()) {
            email.error = resources.getString(R.string.emptyField)
            return
        } else
            viewModel.userModel!!.email = email.text.toString()

        if (code.text.toString().isEmpty()) {
            code.error = resources.getString(R.string.emptyField)
            return
        } else
            viewModel.userModel!!.phonecode = code.text.toString()

        if (phone.text.toString().isEmpty()) {
            phone.error = resources.getString(R.string.emptyField)
            return
        } else
            viewModel.userModel!!.phone = phone.text.toString()

        val user = viewModel.userModel
        if (user != null) {
            val updateProfileBody = UpdateProfileBody(
                user.firstName!!,
                user.lastName!!,
                user.username!!,
                user.email!!,
                user.phone!!,
                user.phonecode!!,
                user.gender!!.toString(),
                user.countryCode!!,
                user.state!!.toString(),
                if (newPassword.text.isNotEmpty()) newPassword.text.toString() else null,
                if (oldPassword.text.isNotEmpty()) oldPassword.text.toString() else null,
                user.city!!.toString()
            )
            val imageFile = viewModel.getUserImageFile()

            viewModel.updateProfile(updateProfileBody, imageFile)
        }

    }

    @AfterPermissionGranted(RC_PERMISSION_STORAGE)
    private fun checkStorage() {
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            // Already have permission, do the thing

            updateProfile()

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.requestPermission),
                RC_PERMISSION_STORAGE, *perms
            )
        }
    }

    private fun chooseAvatar() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        startActivityForResult(intent, RC_IMAGES)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                RC_IMAGES -> {
                    val uri = data?.data
                    val path = activity?.getRealPathFromUri(uri!!)
                    if (path != null) {
                        profileUserImage.setImageURI(uri)
                        viewModel.userImagePath = path
                    } else {
                        activity?.toast(getString(R.string.errorSelectImage))
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


}
