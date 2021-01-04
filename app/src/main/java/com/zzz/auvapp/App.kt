package com.zzz.auvapp

import android.content.Context
import androidx.multidex.MultiDex
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zzz.common.BaseApp

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2020/12/31
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class App: BaseApp() {

    init {
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            layout.setEnableLoadMore(true)
            layout.setEnableLoadMoreWhenContentNotFull(true)
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setEnableHeaderTranslationContent(true)
            MaterialHeader(context).setColorSchemeResources(R.color.blue, R.color.blue, R.color.blue)
        }

        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            layout.setEnableFooterFollowWhenNoMoreData(true)
            layout.setEnableFooterTranslationContent(true)
            layout.setFooterHeight(53f)
            layout.setFooterTriggerRate(0.6f)
            ClassicsFooter.REFRESH_FOOTER_NOTHING = "- The End -"
            ClassicsFooter(context).apply {
                setAccentColorId(R.color.colorTextPrimary)
            }
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

    }
}