package com.zzz.auvapp.ui.home.discovery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zzz.auvapp.logic.Repository
import com.zzz.auvapp.logic.model.HomePageDiscovery
import com.zzz.auvapp.logic.network.EyeService
import com.zzz.common.ext.TransformUtils
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
class DiscoveryHomeViewModel : BaseViewModel() {

    val dataList = ArrayList<HomePageDiscovery.Item>()

    private var nextUrl = ""

    fun setNextUrl(url: String) {
        nextUrl = url
    }

    private val requestUrl = MutableLiveData<String>()

    fun isRefresh() = EyeService.HOMEPAGE_DISCOVERY_URL == requestUrl.value

    fun refresh() {
        requestUrl.value = EyeService.HOMEPAGE_DISCOVERY_URL
    }

    fun loadMore() {
        requestUrl.value = nextUrl
    }

    val requestUrlLiveData = Transformations.switchMap(requestUrl) {
        Repository.getHomeDiscovery(it)
    }

}