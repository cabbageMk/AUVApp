package com.zzz.auvapp.ui.home

import android.view.View
import com.zzz.auvapp.ui.BaseViewPagerFragment
import com.zzz.auvapp.ui.main.MainActivity

class HomeFragment(): BaseViewPagerFragment() {

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
    }

    override fun initData() {
        super.initData()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}