package com.zzz.auvapp.logic

import androidx.lifecycle.liveData
import com.zzz.auvapp.logic.model.HomePageRecommend
import com.zzz.auvapp.logic.network.EyeNetWork
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
object Repository {

    fun getHomeRecommend(url: String) = fire(Dispatchers.IO) {
        val homeRecommoned = EyeNetWork.getHomeRecommoned(url)
        if (homeRecommoned.count > 0) {
            Result.success(homeRecommoned)
        } else {
            Result.failure(RuntimeException("homeRecommoned response data is empty"))
        }
    }

    fun getHomeDiscovery(url: String) = fire(Dispatchers.IO) {
        val homeDiscovery = EyeNetWork.getHomeDiscovery(url)
        if (homeDiscovery.count > 0) {
            Result.success(homeDiscovery)
        } else {
            Result.failure(RuntimeException("homeDiscovery response data is empty"))
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)
        }
}