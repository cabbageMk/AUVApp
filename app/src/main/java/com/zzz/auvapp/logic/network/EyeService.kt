package com.zzz.auvapp.logic.network

import com.zzz.auvapp.logic.model.CommunityRecommend
import com.zzz.auvapp.logic.model.HomePageDaily
import com.zzz.auvapp.logic.model.HomePageDiscovery
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

    /**
     * 首页-发现列表
     */
    @GET
    fun getHomePageDiscovery(@Url url: String): Call<HomePageDiscovery>

    /**
     * 首页-日报列表
     */
    @GET
    fun getHomePageDaily(@Url url: String): Call<HomePageDiscovery>

    @GET
    fun getCommunityRec(@Url url: String): Call<CommunityRecommend>

    companion object {

        /**
         * 首页-推荐列表
         */
        const val HOMEPAGE_RECOMMEND_URL =
            "${RequestServiceCreator.BASE_URL}api/v5/index/tab/allRec?page=0"

        /**
         * 首页-发现列表
         */
        const val HOMEPAGE_DISCOVERY_URL = "${RequestServiceCreator.BASE_URL}api/v7/index/tab/discovery"

        /**
         * 首页-日报列表
         */
        const val HOMEPAGE_DAILY_URL = "${RequestServiceCreator.BASE_URL}api/v5/index/tab/feed"

        /**
         * 首页-日报列表
         */
        const val COMMUNITY_RECOMMEND_URL = "${RequestServiceCreator.BASE_URL}api/v7/community/tab/rec"
    }
}