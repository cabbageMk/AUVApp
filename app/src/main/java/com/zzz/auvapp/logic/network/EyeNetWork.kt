package com.zzz.auvapp.logic.network

import com.zzz.common.ext.await
import com.zzz.common.net.RequestServiceCreator

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
object EyeNetWork {

    private val eyeService = RequestServiceCreator.create(EyeService::class.java)


    suspend fun getHomeRecommoned(url: String) = eyeService.getHomePageRecommend(url).await()

    suspend fun getHomeDiscovery(url: String) = eyeService.getHomePageDiscovery(url).await()

    suspend fun getHomeDaily(url: String) = eyeService.getHomePageDaily(url).await()

    suspend fun getCommunityRec(url: String) = eyeService.getCommunityRec(url).await()

}