package com.cezma.app.ui.mainActivity.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.cezma.app.R
import com.cezma.app.utiles.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
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

        handleSocialClicks()

        searchImg.setOnClickListener {
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

        handleLanguageView()

        setAuthState()

    }

    private fun handleSocialClicks() {
        val socialItemId = 15
        val viewClicked = navigationView.menu.getItem(socialItemId).actionView
        viewClicked.findViewById<ImageView>(R.id.facebook).setOnClickListener {
        }

        viewClicked.findViewById<ImageView>(R.id.inesta).setOnClickListener {
        }

        viewClicked.findViewById<ImageView>(R.id.twitter).setOnClickListener {
        }
    }

    private fun handleLanguageView() {
        val languageItemId = 16
        val viewClicked2 = navigationView.menu.getItem(languageItemId).actionView

        when (Injector.getPreferenceHelper().language) {
            Constants.Language.ENGLISH.value -> {
                viewClicked2.findViewById<MaterialButton>(R.id.arabic).visibility = View.VISIBLE
                viewClicked2.findViewById<MaterialButton>(R.id.english).visibility = View.GONE
            }

            Constants.Language.ARABIC.value -> {
                viewClicked2.findViewById<MaterialButton>(R.id.arabic).visibility = View.GONE
                viewClicked2.findViewById<MaterialButton>(R.id.english).visibility = View.VISIBLE
            }
        }

        viewClicked2.findViewById<MaterialButton>(R.id.arabic).setOnClickListener {
            activity?.saveLanguage(Constants.Language.ARABIC)
            activity?.finish()
            activity?.restartApplication()
        }

        viewClicked2.findViewById<MaterialButton>(R.id.english).setOnClickListener {
            activity?.saveLanguage(Constants.Language.ENGLISH)
            activity?.finish()
            activity?.restartApplication()
        }
    }

    private fun setAuthState() {
        val preferencesHelper = Injector.getPreferenceHelper()
        if (preferencesHelper.isLoggedIn) {
            navigationView.menu.getItem(14).title = context?.resources?.getString(R.string.logOut)
        } else {
            navigationView.menu.getItem(14).title =
                context?.resources?.getString(R.string.NewAccount)
        }
    }

    private fun showHomeHeader(selectedBottomItem: Int) {
        when (selectedBottomItem) {
            R.id.action_Home -> {
                searchImg.visibility = View.VISIBLE
                homeFragmentTitleTv.visibility = View.GONE
            }
            R.id.action_Messages -> {
                homeFragmentTitleTv.text = getString(R.string.messages)
                homeFragmentTitleTv.visibility = View.VISIBLE
                searchImg.visibility = View.GONE
            }
            R.id.action_Notifications -> {
                homeFragmentTitleTv.text = getString(R.string.notifications)
                searchImg.visibility = View.GONE
                homeFragmentTitleTv.visibility = View.VISIBLE
            }
            else -> {
                searchImg.visibility = View.VISIBLE
                homeFragmentTitleTv.visibility = View.GONE
            }
        }
    }

    private fun selectNotificationFragment() {
        activity?.findNavController(R.id.homeSubHost)!!.navigate(R.id.notificationsFragment)
        homeFragmentTitleTv.text = getString(R.string.notifications)
        searchImg.visibility = View.GONE
        homeFragmentTitleTv.visibility = View.VISIBLE
    }

    private fun selectMessagesFragment() {
        activity?.findNavController(R.id.homeSubHost)!!.navigate(R.id.messagesFragment)
        homeFragmentTitleTv.text = getString(R.string.messages)
        homeFragmentTitleTv.visibility = View.VISIBLE
        searchImg.visibility = View.GONE
    }

    private fun selectSubHomeFragment() {
        activity?.findNavController(R.id.homeSubHost)!!.navigate(R.id.subHomeFragment)
        searchImg.visibility = View.VISIBLE
        homeFragmentTitleTv.visibility = View.GONE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_Profile -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_profileFragment)
            }
            R.id.nav_AddProduct -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_addProductFragment)
            }
            R.id.nav_AddShop -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_addShopFragment)
            }
            R.id.nav_Upgrade -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_upgradeAccountFragment)
            }
            R.id.nav_Favourites -> {
                if (Injector.getPreferenceHelper().isLoggedIn) {
                    findNavController().navigate(R.id.action_mainHomeFragment_to_favouritesFragment)
                } else {
                    activity?.snackBarWithAction(
                        getString(R.string.you_must_login),
                        getString(R.string.login),
                        rootView
                    ) {
                        findNavController().navigate(R.id.action_mainHomeFragment_to_loginFragment)

                    }
                }
            }
            R.id.nav_Following -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_followingFragment)
            }
            R.id.nav_FollowingShopes -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_followingShopsFragment)
            }
            R.id.nav_Categories -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_categoryFragment)
            }
            R.id.nav_HowToOpenShop -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_howToOpenShopFragment)
            }
            R.id.nav_PrivacyPolicy -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_privacyPolicyFragment)
            }
            R.id.nav_Terms -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_tremsAndConditionsFragment)
            }
            R.id.nav_ContactUs -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_contactUsFragment)
            }
            R.id.nav_AboutUs -> {
                findNavController().navigate(R.id.action_mainHomeFragment_to_aboutUsFragment)
            }
            R.id.nav_LogOut -> {
                if (Injector.getPreferenceHelper().isLoggedIn) {
                    Injector.getPreferenceHelper().clear()
                    setAuthState()
                    //Logout from service still
                    activity?.toast(getString(R.string.logoutSuccess))
                } else {
                    findNavController().navigate(R.id.action_mainHomeFragment_to_loginFragment)
                }
            }

        }
        viewModel.selectedBottomItemId = bottom_navigation.selectedItemId
        rootViewDl.closeDrawer(GravityCompat.START)
        return true
    }

}
