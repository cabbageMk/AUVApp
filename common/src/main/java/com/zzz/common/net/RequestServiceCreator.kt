package com.zzz.common.net

import com.zzz.common.BaseApp
import com.zzz.common.uitls.LogUtil
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object RequestServiceCreator {
    private const val BASE_URL = "http://baobab.kaiyanapp.com/"

    private val TAG = RequestServiceCreator::class.java.simpleName

    val mRetrofit: Retrofit

    private var httpLoggingInterceptor: HttpLoggingInterceptor

    // 缓存文件
    private val cacheFile: File = File(BaseApp.context.cacheDir.absolutePath)

    // 缓存文件大小
    private const val maxSize: Long = 8 * 1024 * 1024

    // OkHttpCache
    private var cache: Cache

    init {
        // 初始化缓存文件
        if (!cacheFile.exists()) cacheFile.mkdir()
        cache = Cache(cacheFile, maxSize)

        httpLoggingInterceptor = HttpLoggingInterceptor(object :HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                LogUtil.d(TAG, "[getRequestService][log] message $message")
            }
        })
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder().apply {
            connectTimeout(10L, TimeUnit.SECONDS)
            readTimeout(30L, TimeUnit.SECONDS)
            writeTimeout(10L, TimeUnit.SECONDS)
            addInterceptor(HeadIntercept())
            addInterceptor(httpLoggingInterceptor)
            cache(cache)
        }

        mRetrofit = Retrofit.Builder().client(builder.build())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    inline fun <reified T> create(): T = mRetrofit.create(T::class.java)

    suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine {
            enqueue(object :Callback<T> {
                override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            it.resume(body)
                        } else {
                            it.resumeWithException(RuntimeException("response body is null"))
                        }
                    } else {
                        it.resumeWithException(RuntimeException("response is failure"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }

            })
        }
    }

    // 可以对请求头参数作统一处理，通过下面的`addHeader()`方法
    class HeadIntercept : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.let {
                it.proceed(
                    it.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json;charset=UTF-8")
                        .addHeader("Date", Date(System.currentTimeMillis()).toGMTString())
                        .build()
                )
            }
        }
    }
}