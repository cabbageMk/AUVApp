package com.zzz.auvapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.FragmentBaseViewpagerBinding
import com.zzz.common.mvvm.BaseVMFragment

abstract class BaseViewPagerFragment :
    BaseVMFragment<FragmentBaseViewpagerBinding>(R.layout.fragment_base_viewpager) {

    abstract val createTitles: ArrayList<CustomTabEntity>

    abstract val createFragments: Array<Fragment>

    private val vpAdapter by lazy { MyPagerAdapter(activity!!).apply { addFragments(createFragments) } }

    private lateinit var pagerChangeCallback: MyPagerChangeCallback

    override fun openObserve() {

    }

    override fun initView() {
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.adapter = vpAdapter

        binding.fragmentBaseViewpagerBar.tabLayout.setTabData(createTitles)
        binding.fragmentBaseViewpagerBar.tabLayout.setOnTabSelectListener(object :
            OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                binding.viewPager.currentItem = position
            }

            override fun onTabReselect(position: Int) {

            }
        })
        pagerChangeCallback = MyPagerChangeCallback()
        binding.viewPager.registerOnPageChangeCallback(pagerChangeCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.viewPager.unregisterOnPageChangeCallback(pagerChangeCallback)
    }

    override fun initData() {

    }

    inner class MyPagerChangeCallback: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            binding.fragmentBaseViewpagerBar.tabLayout.currentTab = position
        }
    }

    inner class MyPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        private val fragments = mutableListOf<Fragment>()

        fun addFragments(fragment: Array<Fragment>) {
            fragments.addAll(fragment)
        }

        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int) = fragments[position]

    }
}