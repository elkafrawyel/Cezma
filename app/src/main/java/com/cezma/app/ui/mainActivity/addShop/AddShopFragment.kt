package com.cezma.app.ui.mainActivity.addShop

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

import com.cezma.app.R
import com.cezma.app.data.model.AddShopBody
import com.cezma.app.data.model.CategoryModel
import com.cezma.app.data.model.SubCategoryModel
import com.cezma.app.utiles.*
import kotlinx.android.synthetic.main.add_shop_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

const val RC_PERMISSION_STORAGE = 111
const val RC_IMAGES_LOGO = 114
const val RC_IMAGES_COVER = 113

class AddShopFragment : Fragment() {

    companion object {
        fun newInstance() = AddShopFragment()
    }

    private lateinit var viewModel: AddShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_shop_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AddShopViewModel::class.java)
        viewModel.uiStateCategories.observe(this, Observer { onCategoriesResponse(it) })
        viewModel.uiStateSubCategories.observe(this, Observer { onSubCategoriesResponse(it) })
        viewModel.uiState.observe(this, Observer { oAddShopResponse(it) })
        backImgv.setOnClickListener {
            findNavController().navigateUp()
        }
        addCoverFL.setOnClickListener {
            chooseCover()
        }

        addLogoFL.setOnClickListener {
            chooseLogo()
        }

        addShopMbtn.setOnClickListener {
            checkStorage()
        }
    }

    private fun oAddShopResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }

            ViewState.Success -> {
                loading.visibility = View.GONE
                activity?.toast(viewModel.addShopMessage)
                findNavController().navigateUp()
            }

            is ViewState.Error -> {
                loading.visibility = View.GONE
            }

            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.snackBar(
                    getString(R.string.noConnection),
                    rootView
                )
            }

            ViewState.Empty -> {
                activity?.toast(getString(R.string.no_subcategories))
            }
        }
    }

    @AfterPermissionGranted(RC_PERMISSION_STORAGE)
    private fun checkStorage() {
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            // Already have permission, do the thing
            addShop()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.requestPermission),
                RC_PERMISSION_STORAGE, *perms
            )
        }
    }

    private fun addShop() {


        if (shopNameTv.text.toString().isEmpty()) {
            shopNameTv.error = resources.getString(R.string.emptyField)
            return
        }

        if (shopUserNameTv.text.toString().isEmpty()) {
            shopUserNameTv.error = resources.getString(R.string.emptyField)
            return
        }

        if (shortDescTv.text.toString().isEmpty()) {
            shortDescTv.error = resources.getString(R.string.emptyField)
            return
        }

        if (longDescTv.text.toString().isEmpty()) {
            longDescTv.error = resources.getString(R.string.emptyField)
            return
        }

        if (viewModel.selectedCategory == null || viewModel.selectedSubCategory == null) {
            activity?.toast(getString(R.string.select_category))
            return
        }

        if (viewModel.logoPath == null || viewModel.coverPath== null) {
            activity?.toast(getString(R.string.select_shop_images))
            return
        }


        val addShopBody = AddShopBody(
            username = shopUserNameTv.text.toString(),
            title = shopNameTv.text.toString(),
            short_desc = shortDescTv.text.toString(),
            long_desc = longDescTv.text.toString(),
            category = viewModel.selectedSubCategory!!.id!!
        )

        viewModel.addShop(addShopBody, viewModel.getLogoImageFile(), viewModel.getCoverImageFile())
    }

    private fun chooseLogo() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        startActivityForResult(intent, RC_IMAGES_LOGO)
    }

    private fun chooseCover() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/*"
        startActivityForResult(intent, RC_IMAGES_COVER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                RC_IMAGES_LOGO -> {
                    val uri = data?.data
                    val path = activity?.getRealPathFromUri(uri!!)
                    if (path != null) {
                        viewModel.logoPath = path
                        activity?.toast(getString(R.string.logoImageSelected))
                    } else {
                        activity?.toast(getString(R.string.errorSelectImage))
                    }
                }

                RC_IMAGES_COVER -> {
                    val uri = data?.data
                    val path = activity?.getRealPathFromUri(uri!!)
                    if (path != null) {
                        viewModel.coverPath = path
                        activity?.toast(getString(R.string.coverImageSelected))
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

    private fun onSubCategoriesResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                val adapterSubCategories =
                    ArrayAdapter<SubCategoryModel>(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        viewModel.subCategories
                    )
                adapterSubCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                subCategoriesSpinner.adapter = adapterSubCategories
                subCategoriesSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            viewModel.selectedSubCategory = viewModel.subCategories[position]
                        }
                    }
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

            ViewState.Empty -> {
                activity?.toast(getString(R.string.no_subcategories))
            }
        }
    }

    private fun onCategoriesResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE

                val adapterCategories =
                    ArrayAdapter<CategoryModel>(
                        context!!,
                        android.R.layout.simple_spinner_item,
                        viewModel.categories
                    )
                adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categoriesSpinner.adapter = adapterCategories

                categoriesSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(parent: AdapterView<*>?) {

                        }

                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            if (viewModel.selectedCategory!!.id != viewModel.categories[position].id) {
                                viewModel.selectedCategory = viewModel.categories[position]
                                viewModel.getSubCategoriesData()
                            }
                        }
                    }
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
        }
    }
}
