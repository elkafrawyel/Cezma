package com.cezma.store.views.mainActivity.subCategory

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import kotlinx.android.synthetic.main.sub_category_fragment.*

class SubCategoryFragment : Fragment() {

    companion object {
        fun newInstance() = SubCategoryFragment()
    }

    private lateinit var viewModel: SubCategoryViewModel
    private val adapterSubCategories= AdapterSubCategories()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sub_category_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SubCategoryViewModel::class.java)


        val subCategories = ArrayList<String>()
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")
        subCategories.add("a")

        adapterSubCategories.replaceData(subCategories)

        subCategoriesRv.adapter = adapterSubCategories
        subCategoriesRv.setHasFixedSize(true)

        subCategoryBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        categoryTitle.setOnClickListener {
            findNavController().navigate(R.id.action_subCategoryFragment_to_productsFragment)
        }

        searchTv.setOnClickListener {
            findNavController().navigate(R.id.action_subCategoryFragment_to_searchFragment)
        }
    }

}
