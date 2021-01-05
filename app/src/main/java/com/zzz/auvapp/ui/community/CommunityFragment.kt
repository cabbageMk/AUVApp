package com.zzz.auvapp.ui.community

import android.view.View
import androidx.fragment.app.Fragment
import com.flyco.tablayout.listener.CustomTabEntity
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.TabEntity
import com.zzz.auvapp.ui.BaseViewPagerFragment
import com.zzz.auvapp.ui.notification.TestFragment
import com.zzz.common.BaseApp
import com.zzz.common.ext.gone

class CommunityFragment: BaseViewPagerFragment() {
    override val createTitles = ArrayList<CustomTabEntity>().apply {
        add(TabEntity(BaseApp.context.getString(R.string.commend)))
        add(TabEntity(BaseApp.context.getString(R.string.follow)))
    }
    override val createFragments: Array<Fragment>
        get() = arrayOf(TestFragment.newInstance(BaseApp.context.getString(R.string.commend)), TestFragment.newInstance(BaseApp.context.getString(R.string.follow)))

    override fun openObserve() {
        super.openObserve()

    }

    override fun initView() {
        super.initView()
        binding.fragmentBaseViewpagerBar.ivDrawer.gone()
    }

    override fun initData() {
        super.initData()
    }

    companion object {

        @JvmStatic
        fun newInstance() = CommunityFragment()
    }
}