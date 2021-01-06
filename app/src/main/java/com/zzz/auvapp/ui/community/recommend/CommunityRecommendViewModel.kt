package com.zzz.auvapp.ui.community.recommend

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zzz.auvapp.logic.Repository
import com.zzz.auvapp.logic.model.CommunityRecommend
import com.zzz.auvapp.logic.network.EyeService
import com.zzz.common.mvvm.BaseViewModel

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CommunityRecommendViewModel: BaseViewModel() {

    val dataList = ArrayList<CommunityRecommend.Item>()

    private var nextUrl = ""

    fun setNextUrl(url: String) {
        nextUrl = url
    }

    private val requestUrl = MutableLiveData<String>()

    fun refresh() {
        requestUrl.value = EyeService.COMMUNITY_RECOMMEND_URL
    }

    fun loadMore() {
        requestUrl.value = nextUrl
    }

    fun isRefresh() = requestUrl.value == EyeService.COMMUNITY_RECOMMEND_URL

    val requestLiveData = Transformations.switchMap(requestUrl) {
        Repository.getCommunityRec(it)
    }
}