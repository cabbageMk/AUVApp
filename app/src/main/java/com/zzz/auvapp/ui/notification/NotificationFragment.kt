package com.zzz.auvapp.ui.notification

import android.view.View
import androidx.fragment.app.Fragment
import com.flyco.tablayout.listener.CustomTabEntity
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.TabEntity
import com.zzz.auvapp.ui.BaseViewPagerFragment
import com.zzz.auvapp.ui.notification.push.PushFragment
import com.zzz.common.BaseApp
import com.zzz.common.ext.gone

class NotificationFragment: BaseViewPagerFragment() {

    override val createTitles = ArrayList<CustomTabEntity>().apply {
        add(TabEntity(BaseApp.context.getString(R.string.push)))
        add(TabEntity(BaseApp.context.getString(R.string.interaction)))
        add(TabEntity(BaseApp.context.getString(R.string.inbox)))
    }
    override val createFragments: Array<Fragment>
        get() = arrayOf(PushFragment(),
            TestFragment.newInstance(BaseApp.context.getString(R.string.interaction)),TestFragment.newInstance(BaseApp.context.getString(R.string.inbox)))

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
        fun newInstance() = NotificationFragment()
    }
}