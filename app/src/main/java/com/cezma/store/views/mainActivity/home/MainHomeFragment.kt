package com.cezma.store.views.mainActivity.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.cezma.store.R
import com.cezma.store.utiles.toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main_home_fragment.*

class MainHomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance() = MainHomeFragment()
    }

    private lateinit var viewModel: MainHomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainHomeFragmentViewModel::class.java)

        navigationView.setNavigationItemSelectedListener(this)
        bottom_navigation.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.action_Home -> {
                        selectSubHomeFragment()
                    }
                    R.id.action_Messages -> {
                        selectMessagesFragment()
                    }
                    R.id.action_Notifications -> {
                        selectNotificationFragment()
                    }
                    else -> {
                    }
                }
                return@OnNavigationItemSelectedListener true
            })

        drawerToggleImgBtn.setOnClickListener {
            rootViewDl.openDrawer(GravityCompat.START)
        }

        val socialItemId = 15
        val viewClicked = navigationView.menu.getItem(socialItemId).actionView
        viewClicked.findViewById<ImageView>(R.id.facebook).setOnClickListener {
            activity?.toast("Facebook")
        }

        viewClicked.findViewById<ImageView>(R.id.inesta).setOnClickListener {
            activity?.toast("Instegram")
        }

        viewClicked.findViewById<ImageView>(R.id.twitter).setOnClickListener {
            activity?.toast("Twitter")
        }

        searchTv.setOnClickListener {
            viewModel.selectedBottomItemId = bottom_navigation.selectedItemId
            findNavController().navigate(R.id.action_mainHomeFragment_to_searchFragment)
        }

        if (!viewModel.isOpened) {
            //set initial view as sub home fragment in side home fragment
            selectSubHomeFragment()
            viewModel.isOpened = true
        } else {
            showHomeHeader(viewModel.selectedBottomItemId)
        }
    }

    private fun showHomeHeader(selectedBottomItem: Int) {
        when (selectedBottomItem) {
            R.id.action_Home -> {
                searchTv.visibility = View.VISIBLE
                homeFragmentTitleTv.visibility = View.GONE
            }
            R.id.action_Messages-> {
                homeFragmentTitleTv.text = getString(R.string.messages)
                homeFragmentTitleTv.visibility = View.VISIBLE
                searchTv.visibility = View.GONE
            }
            R.id.action_Notifications -> {
                homeFragmentTitleTv.text = getString(R.string.notifications)
                searchTv.visibility = View.GONE
                homeFragmentTitleTv.visibility = View.VISIBLE
            }
            else ->{
                searchTv.visibility = View.VISIBLE
                homeFragmentTitleTv.visibility = View.GONE
            }
        }
    }

    private fun selectNotificationFragment() {
        activity?.findNavController(R.id.homeSubHost)!!.navigate(R.id.notificationsFragment)
        homeFragmentTitleTv.text = getString(R.string.notifications)
        searchTv.visibility = View.GONE
        homeFragmentTitleTv.visibility = View.VISIBLE
    }

    private fun selectMessagesFragment() {
        activity?.findNavController(R.id.homeSubHost)!!.navigate(R.id.messagesFragment)
        homeFragmentTitleTv.text = getString(R.string.messages)
        homeFragmentTitleTv.visibility = View.VISIBLE
        searchTv.visibility = View.GONE
    }

    private fun selectSubHomeFragment() {
        activity?.findNavController(R.id.homeSubHost)!!.navigate(R.id.subHomeFragment)
        searchTv.visibility = View.VISIBLE
        homeFragmentTitleTv.visibility = View.GONE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.nav_Profile -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_profileFragment)
//            }
//            R.id.nav_AddProduct -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_addProductFragment)
//            }
//            R.id.nav_AddShop -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_addShopFragment)
//            }
//            R.id.nav_Upgrade -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_upgradeAccountFragment)
//            }
//            R.id.nav_Favourites -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_favouritesFragment)
//            }
//            R.id.nav_Following -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_followingFragment)
//            }
//            R.id.nav_FollowingShopes -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_followingShopsFragment)
//            }
//            R.id.nav_Categories -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_categoryFragment)
//            }
//            R.id.nav_HowToOpenShop -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_howToOpenShopFragment)
//            }
//            R.id.nav_PrivacyPolicy -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_privacyPolicyFragment)
//            }
//            R.id.nav_Terms -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_tremsAndConditionsFragment)
//            }
//            R.id.nav_ContactUs -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_contactUsFragment)
//            }
//            R.id.nav_AboutUs -> {
//                findNavController().navigate(R.id.action_mainHomeFragment_to_aboutUsFragment)
//            }
//            R.id.nav_LogOut -> {
//
//            }
//
//        }
//        viewModel.selectedBottomItemId = bottom_navigation.selectedItemId
//        rootViewDl.closeDrawer(GravityCompat.START)
        return true
    }

}
