package com.zzz.auvapp.logic.network

import com.zzz.auvapp.logic.model.HomePageRecommend
import com.zzz.common.net.RequestServiceCreator
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
interface EyeService {

    /**
     * 首页-推荐列表
     */
    @GET
    fun getHomePageRecommend(@Url url: String): Call<HomePageRecommend>

    companion object {

        /**
         * 首页-推荐列表
         */
        const val HOMEPAGE_RECOMMEND_URL =
            "${RequestServiceCreator.BASE_URL}api/v5/index/tab/allRec?page=0"
    }
}