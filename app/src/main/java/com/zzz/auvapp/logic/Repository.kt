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
            Result.failure(RuntimeException("response data is empty"))
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