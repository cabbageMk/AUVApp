package com.zzz.common.net

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException

fun <T> CoroutineScope.retrofit(
    dsl: RetrofitCoroutineDsl<T>.() -> Unit
){
    this.launch(Dispatchers.Main) {
        val retrofitCoroutine = RetrofitCoroutineDsl<T>()
        retrofitCoroutine.dsl()

        retrofitCoroutine.api?.let { it ->
            val work = this.async(Dispatchers.IO) { // io线程执行
                try {
                    it.execute()
                } catch (e: ConnectException) {
                    e.printStackTrace()
                    retrofitCoroutine.onFailed?.invoke("网络连接出错", -100)
                    null
                } catch (e: IOException) {
                    retrofitCoroutine.onFailed?.invoke("未知错误", -1)
                    null
                }
            }
            work.invokeOnCompletion { _ ->
                // 协程关闭时，取消任务
                if (work.isCancelled) {
                    it.cancel()
                    retrofitCoroutine.clean()
                }
            }
            val response = work.await()
            retrofitCoroutine.onComplete?.invoke()
            response?.let {
                if (response.isSuccessful) {
                    retrofitCoroutine.onSuccess?.invoke(response.body())
                } else {
                    // 处理 HTTP code
                    when (response.code()) {
                        401 -> {
                        }
                        500 -> {
                        }
                    }
                    retrofitCoroutine.onFailed?.invoke(response.errorBody()?.toString(), response.code())
                }
            }
        }
    }
}