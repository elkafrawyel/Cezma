package com.cezma.app.ui.mainActivity.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.cezma.app.R
import com.cezma.app.utiles.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.koraextra.app.utily.observeEvent
import kotlinx.android.synthetic.main.main_home_fragment.*
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView


class MainHomeFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newInstance() = MainHomeFragment()

        private const val ID_HOME = 1
        private const val ID_MESSAGE = 2
        private const val ID_NOTIFICATION = 3

        private const val HOME_INDEX = 0
        private const val MESSAGE_INDEX = 1
        private const val NOTIFICATION_INDEX = 2
    }

    private var messagesBadge: Badge? = null
    private var notificationBadge: Badge? = null
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
        viewModel.uiStateLogOut.observeEvent(this) { onLogOut(it) }
        viewModel.uiStateNotification.observe(this, Observer { onNotificationResponse(it) })
        navigationView.setNavigationItemSelectedListener(this)

        bottom_navigation.enableAnimation(false)

        bottom_navigation.onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.action_Home -> {
                        viewModel.selectedBottomItemId = ID_HOME
                        selectSubHomeFragment()
                    }
                    R.id.action_Messages -> {
                        viewModel.selectedBottomItemId = ID_MESSAGE
                        selectMessagesFragment()
                        removeBadgeAt(MESSAGE_INDEX)
                    }
                    R.id.action_Notifications -> {
                        viewModel.selectedBottomItemId = ID_NOTIFICATION
                        selectNotificationFragment()
                        removeBadgeAt(NOTIFICATION_INDEX)
                    }
                    else -> {
                    }
                }
                return@OnNavigationItemSelectedListener true
            }

        drawerToggleImgBtn.setOnClickListener {
            rootViewDl.openDrawer(GravityCompat.START)
        }
        handleSocialClicks()

        searchImg.setOnClickListener {
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

//        addBadgeAt(MESSAGE_INDEX, 5)
//
//        addBadgeAt(NOTIFICATION_INDEX, 10)
    }

    private fun onNotificationResponse(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {

            }
            ViewState.Success -> {
                addBadgeAt(NOTIFICATION_INDEX, viewModel.unreadNotisCount)
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

    private fun addBadgeAt(position: Int, number: Int) {

        when (position) {
            HOME_INDEX -> {

            }

            MESSAGE_INDEX -> {
                // add badge
                messagesBadge = QBadgeView(context)
                    .setBadgeNumber(number)
                    .setGravityOffset(12F, 2F, true)
                    .setOnDragStateChangedListener { _, _, _ -> }
                    .bindTarget(bottom_navigation.getBottomNavigationItemView(position))

            }

            NOTIFICATION_INDEX -> {
                notificationBadge = QBadgeView(context)
                    .setBadgeNumber(number)
                    .setGravityOffset(12F, 2F, true)
                    .setOnDragStateChangedListener { _, _, _ -> }
                    .bindTarget(bottom_navigation.getBottomNavigationItemView(position))
            }
        }
    }

    private fun removeBadgeAt(position: Int) {
        when (position) {
            HOME_INDEX -> {

            }

            MESSAGE_INDEX -> {
                if (messagesBadge != null) {
                    messagesBadge!!.hide(false)
                }
            }

            NOTIFICATION_INDEX -> {
                if (notificationBadge != null) {
                    notificationBadge!!.hide(false)
                }
            }
        }
    }

    private fun onLogOut(it: ViewState?) {
        when (it) {
            ViewState.Loading -> {
                loading.visibility = View.VISIBLE
            }
            ViewState.Success -> {
                loading.visibility = View.GONE
                activity?.toast(viewModel.logOutMessage)
                Injector.getPreferenceHelper().clear()
                setAuthState()
            }
            is ViewState.Error -> {
                loading.visibility = View.GONE
                activity?.snackBar(it.message, rootView)
            }
            ViewState.NoConnection -> {
                loading.visibility = View.GONE
                activity?.snackBarWithAction(
                    getString(R.string.noConnection),
                    getString(R.string.retry),
                    rootView
                ) {
                    viewModel.logOut()
                }
            }
            ViewState.Empty -> {

            }
            null -> {

            }
        }
    }

    private fun handleSocialClicks() {
        val socialItemId = 16
        val viewClicked = navigationView.menu.getItem(socialItemId).actionView
        viewClicked.findViewById<ImageView>(R.id.facebook).setOnClickListener {
        }

        viewClicked.findViewById<ImageView>(R.id.inesta).setOnClickListener {
        }

        viewClicked.findViewById<ImageView>(R.id.twitter).setOnClickListener {
        }
    }

    private fun handleLanguageView() {
        val languageItemId = 17
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
            navigationView.menu.getItem(15).title = context?.resources?.getString(R.string.logOut)
        } else {
            navigationView.menu.getItem(15).title =
                context?.resources?.getString(R.string.NewAccount)
        }
    }

    private fun showHomeHeader(selectedBottomItem: Int) {
        when (selectedBottomItem) {
            1 -> {
                searchImg.visibility = View.VISIBLE
                homeFragmentTitleTv.visibility = View.GONE
            }
            2 -> {
                homeFragmentTitleTv.text = getString(R.string.messages)
                homeFragmentTitleTv.visibility = View.VISIBLE
                searchImg.visibility = View.GONE
            }
            3 -> {
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
                if (Injector.getPreferenceHelper().isLoggedIn) {
                    findNavController().navigate(R.id.action_mainHomeFragment_to_profileFragment)
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
            R.id.nav_offers -> {
                if (Injector.getPreferenceHelper().isLoggedIn) {
                    findNavController().navigate(R.id.action_mainHomeFragment_to_offersFragment)
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
                    viewModel.logOut()
                } else {
                    findNavController().navigate(R.id.action_mainHomeFragment_to_loginFragment)
                }
            }
        }
        rootViewDl.closeDrawer(GravityCompat.START)
        return true
    }

}
