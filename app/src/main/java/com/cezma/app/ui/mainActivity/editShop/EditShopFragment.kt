package com.cezma.app.ui.mainActivity.editShop

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
import com.cezma.app.data.model.AddShopBody
import com.cezma.app.data.model.CategoryModel
import com.cezma.app.data.model.SubCategoryModel
import com.cezma.app.ui.mainActivity.addShop.RC_IMAGES_COVER
import com.cezma.app.ui.mainActivity.addShop.RC_IMAGES_LOGO
import com.cezma.app.ui.mainActivity.addShop.RC_PERMISSION_STORAGE
import com.cezma.app.utiles.ViewState
import com.cezma.app.utiles.getRealPathFromUri
import com.cezma.app.utiles.snackBarWithAction
import com.cezma.app.utiles.toast
import kotlinx.android.synthetic.main.edit_shop_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class EditShopFragment : Fragment() {

    companion object {
        fun newInstance() = EditShopFragment()
    }

    private lateinit var viewModel: EditShopViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_shop_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditShopViewModel::class.java)
        viewModel.uiStateCategories.observe(this, Observer { onCategoriesResponse(it) })
        viewModel.uiStateSubCategories.observe(this, Observer { onSubCategoriesResponse(it) })
        viewModel.uiStateMyStore.observe(this, Observer { onyStoreResponse(it) })
        viewModel.uiState.observe(this, Observer { onEditResponse(it) })
        backImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        shopCoverImage.setOnClickListener {
            chooseCover()
        }

        shopLogoImage.setOnClickListener {
            chooseLogo()
        }

        saveShopMbtn.setOnClickListener {
            checkStorage()
        }
    }

    private fun onEditResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE

                activity?.toast(viewModel.editShopMessage)
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

    private fun onyStoreResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE

                //change spinner selection
                setMyStoreData()
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

    private fun setMyStoreData() {
        val myStore = viewModel.myStore
        if (myStore != null){
            Glide.with(context!!).load(myStore.cover).into(shopCoverImage)
            Glide.with(context!!).load(myStore.logo).into(shopLogoImage)
            shopNameTv.setText(myStore.title)
            shopUserNameTv.setText(myStore.username)
            shortDescTv.setText(myStore.shortDesc)
            longDescTv.setText(myStore.longDesc)

            viewModel.categories.forEachIndexed { i, categoryModel ->
                if (myStore.maincategoryId == categoryModel.id){
                    categoriesSpinner.setSelection(i)
                }
            }

            viewModel.subCategories.forEachIndexed { i, subCategoryModel ->
                if (myStore.subcategoryId == subCategoryModel.id){
                    subCategoriesSpinner.setSelection(i)
                }
            }
        }
    }

    @AfterPermissionGranted(RC_PERMISSION_STORAGE)
    private fun checkStorage() {
        val perms = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
            // Already have permission, do the thing
            saveShop()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.requestPermission),
                RC_PERMISSION_STORAGE, *perms
            )
        }
    }

    private fun saveShop() {
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

        val addShopBody = AddShopBody(
            username = shopUserNameTv.text.toString(),
            title = shopNameTv.text.toString(),
            short_desc = shortDescTv.text.toString(),
            long_desc = longDescTv.text.toString(),
            category = viewModel.selectedSubCategory!!.id!!
        )

        viewModel.editStore(addShopBody, viewModel.getLogoImageFile(), viewModel.getCoverImageFile())

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
                        shopLogoImage.setImageURI(uri)
                    } else {
                        activity?.toast(getString(R.string.errorSelectImage))
                    }
                }

                RC_IMAGES_COVER -> {
                    val uri = data?.data
                    val path = activity?.getRealPathFromUri(uri!!)
                    if (path != null) {
                        viewModel.coverPath = path
                        shopCoverImage.setImageURI(uri)
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

                if (viewModel.myStore == null) {
                    viewModel.getMyStore()
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
