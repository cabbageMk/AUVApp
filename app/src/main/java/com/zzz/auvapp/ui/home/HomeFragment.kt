package com.zzz.auvapp.ui.home

import android.view.View
import androidx.fragment.app.Fragment
import com.flyco.tablayout.listener.CustomTabEntity
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.TabEntity
import com.zzz.auvapp.ui.BaseViewPagerFragment
import com.zzz.auvapp.ui.home.discovery.HomeDiscoveryFragment
import com.zzz.auvapp.ui.home.recommend.RecommendHomeFragment
import com.zzz.auvapp.ui.main.MainActivity
import com.zzz.auvapp.ui.notification.TestFragment
import com.zzz.common.BaseApp

class HomeFragment(): BaseViewPagerFragment() {
    override val createTitles = ArrayList<CustomTabEntity>().apply {
        add(TabEntity(BaseApp.context.getString(R.string.discovery)))
        add(TabEntity(BaseApp.context.getString(R.string.commend)))
        add(TabEntity(BaseApp.context.getString(R.string.daily)))
    }
    override val createFragments: Array<Fragment>
        get() = arrayOf(
            HomeDiscoveryFragment(),
            RecommendHomeFragment(), TestFragment.newInstance(BaseApp.context.getString(R.string.daily)))

    override fun openObserve() {
        super.openObserve()

    }

    override fun initView() {
        super.initView()
        binding.fragmentBaseViewpagerBar.ivDrawer.visibility = View.VISIBLE
        binding.fragmentBaseViewpagerBar.ivDrawer.setOnClickListener {
            if (activity is MainActivity) {
                (activity as MainActivity).openDrawer()
            }
        }
        binding.viewPager.currentItem = 1
    }

    override fun initData() {
        super.initData()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}