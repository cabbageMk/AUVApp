package com.zzz.auvapp.ui.home.recommend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zzz.auvapp.logic.Repository
import com.zzz.auvapp.logic.model.HomePageRecommend
import com.zzz.auvapp.logic.network.EyeService
import com.zzz.common.mvvm.BaseViewModel

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RecommendHomeViewModel : BaseViewModel() {
    var nextUrl = ""

    private var requestUrl = MutableLiveData<String>()

    val dataList = ArrayList<HomePageRecommend.Item>()

    fun refresh() {
        requestUrl.value = EyeService.HOMEPAGE_RECOMMEND_URL
    }

    fun loadMore() {
        requestUrl.value = nextUrl ?: ""
    }

    val dataListLiveData = Transformations.switchMap(requestUrl) {
        Repository.getHomeRecommend(it)
    }

    fun isRefresh(): Boolean {
        return EyeService.HOMEPAGE_RECOMMEND_URL == requestUrl.value
    }
}