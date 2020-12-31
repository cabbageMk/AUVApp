package com.zzz.auvapp.ui.community

import android.view.View
import com.zzz.auvapp.ui.BaseViewPagerFragment

class CommunityFragment: BaseViewPagerFragment() {

    override fun openObserve() {
        super.openObserve()

    }

    override fun initView() {
        super.initView()
        binding.fragmentBaseViewpagerBar.ivDrawer.visibility = View.GONE
    }

    override fun initData() {
        super.initData()
    }

    companion object {

        @JvmStatic
        fun newInstance() = CommunityFragment()
    }
}