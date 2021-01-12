package com.zzz.auvapp.ui.notification.push

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zzz.auvapp.logic.Repository
import com.zzz.auvapp.logic.model.PushMessage
import com.zzz.auvapp.logic.network.EyeService
import com.zzz.common.mvvm.BaseViewModel

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PushViewModel: BaseViewModel() {

    val dataList = ArrayList<PushMessage.Message>()

    private var nextUrl = ""

    fun setNextUrl(url: String) {
        nextUrl = url
    }

    private val requestUrl = MutableLiveData<String>()

    fun refresh() {
        requestUrl.value = EyeService.PUSHMESSAGE_URL
    }

    fun loadMore() {
        requestUrl.value = nextUrl
    }

    fun isRefresh() = requestUrl.value == EyeService.PUSHMESSAGE_URL

    val requestLiveData = Transformations.switchMap(requestUrl) {
        Repository.getPushMessage(it)
    }
}