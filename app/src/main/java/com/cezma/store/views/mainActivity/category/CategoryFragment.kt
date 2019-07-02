package com.cezma.store.views.mainActivity.category

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import kotlinx.android.synthetic.main.category_fragment.*

class CategoryFragment : Fragment() {

    companion object {
        fun newInstance() = CategoryFragment()
    }

    private lateinit var viewModel: CategoryViewModel
    private val adapterCategories = AdapterCategories()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        val categories = ArrayList<String>()
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")
        categories.add("a")

        adapterCategories.replaceData(categories)

        categoriesRv.adapter = adapterCategories
        categoriesRv.setHasFixedSize(true)

        categoryBackImgv.setOnClickListener {
            findNavController().navigateUp()
        }

        categoriesTv.setOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_subCategoryFragment)
        }
    }

}
