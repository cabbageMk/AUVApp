package com.zzz.auvapp.ui.main

import android.annotation.SuppressLint
import android.view.Gravity
import androidx.fragment.app.Fragment
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.ActivityMainBinding
import com.zzz.auvapp.ui.community.CommunityFragment
import com.zzz.auvapp.ui.home.HomeFragment
import com.zzz.auvapp.ui.notification.NotificationFragment
import com.zzz.common.mvvm.BaseVMActivity

class MainActivity : BaseVMActivity() {
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private val homeFragment by lazy { HomeFragment.newInstance() }
    private val communityFragment by lazy { CommunityFragment.newInstance() }
    private val notificationFragment by lazy { NotificationFragment.newInstance() }
    private lateinit var fragments: ArrayList<Fragment>
    private lateinit var viewModel: MainViewModel
    private var currentFragment: Fragment? = null

    override fun openObserve() {
        viewModel = MainViewModel()

        viewModel.currentIndex.observe(this) {
            changeFragment(it)
        }
    }

    @SuppressLint("WrongConstant")
    override fun initView() {
        setStatusBarBackground(R.color.purple_700)
        fragments = arrayListOf()
        fragments.apply {
            add(homeFragment)
            add(communityFragment)
            add(notificationFragment)
        }
        binding.bottomnav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentIndex(0)
                R.id.community -> setCurrentIndex(1)
                R.id.notification -> setCurrentIndex(2)
                else -> setCurrentIndex(0)
            }
        }
    }

    private fun setCurrentIndex(i: Int): Boolean {
        viewModel.setCurrentIndex(i)
        return true
    }

    override fun initData() {

    }

    @SuppressLint("WrongConstant")
    fun openDrawer() {
        binding.mainDrawer.openDrawer(Gravity.START)
    }

    private fun changeFragment(i: Int) {
        val targetfragment = fragments[i]
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.apply {
            currentFragment?.let {
                hide(currentFragment!!)
            }
            setReorderingAllowed(true)
            if (targetfragment.isAdded) {
                show(targetfragment).commit()
            } else {
                add(R.id.homeActivityFragContainer, targetfragment).commit()
            }
        }
        currentFragment = targetfragment
    }
}