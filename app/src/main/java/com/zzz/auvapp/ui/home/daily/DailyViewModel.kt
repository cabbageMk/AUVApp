package com.zzz.auvapp.ui.home.daily

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zzz.auvapp.logic.Repository
import com.zzz.auvapp.logic.model.HomePageDaily
import com.zzz.auvapp.logic.model.HomePageDiscovery
import com.zzz.auvapp.logic.network.EyeService
import com.zzz.common.mvvm.BaseViewModel

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class DailyViewModel : BaseViewModel() {

    val dataList = ArrayList<HomePageDiscovery.Item>()

    private var nextUrl = ""

    fun setNextUrl(url: String) {
        nextUrl = url
    }

    private val requestUrl = MutableLiveData<String>()

    fun isRefresh() = requestUrl.value == EyeService.HOMEPAGE_DAILY_URL

    fun refresh() {
        requestUrl.value = EyeService.HOMEPAGE_DAILY_URL
    }

    fun loadMore() {
        requestUrl.value = nextUrl
    }

    val requestUrlLiveData = Transformations.switchMap(requestUrl) {
        Repository.getHomeDaily(it)
    }
}