package com.zzz.auvapp.ui.community.follow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zzz.auvapp.logic.Repository
import com.zzz.auvapp.logic.model.Follow
import com.zzz.auvapp.logic.network.EyeService
import com.zzz.common.mvvm.BaseViewModel

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class FollowViewModel : BaseViewModel() {

    val dataList = ArrayList<Follow.Item>()

    private var nextUrl = ""

    fun setNextUrl(url: String?) {
        nextUrl = url ?: ""
    }

    private val requestUrl = MutableLiveData<String>()

    fun refresh() {
        requestUrl.value = EyeService.FOLLOW_URL
    }

    fun loadMore() {
        requestUrl.value = nextUrl
    }

    fun isRefresh() = requestUrl.value == EyeService.FOLLOW_URL

    val requestLiveData = Transformations.switchMap(requestUrl) {
        Repository.getCommunityFollow(it)
    }
}