package com.zzz.auvapp.logic

import androidx.lifecycle.liveData
import com.zzz.auvapp.logic.model.HomePageRecommend
import com.zzz.auvapp.logic.model.VideoDetail
import com.zzz.auvapp.logic.network.EyeNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
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

    fun getHomeDaily(url: String) = fire(Dispatchers.IO) {
        val homeDaily = EyeNetWork.getHomeDaily(url)
        if (homeDaily.count > 0) {
            Result.success(homeDaily)
        } else {
            Result.failure(RuntimeException("homeDaily response data is empty"))
        }
    }

    fun getCommunityRec(url: String) = fire(Dispatchers.IO) {
        val communityRec = EyeNetWork.getCommunityRec(url)
        if(communityRec.count > 0) {
            Result.success(communityRec)
        } else {
            Result.failure(RuntimeException("communityRec response data is empty"))
        }
    }

    fun getCommunityFollow(url: String) = fire(Dispatchers.IO) {
        val communityFollow = EyeNetWork.getCommunityFollow(url)
        if (communityFollow.count > 0) {
            Result.success(communityFollow)
        } else {
            Result.failure(RuntimeException("communityFollow response data is empty"))
        }
    }

    fun getPushMessage(url: String) = fire(Dispatchers.IO) {
        val pushMessage = EyeNetWork.getPushMessage(url)
        if (pushMessage.messageList.isNullOrEmpty()) {
            Result.failure(RuntimeException("pushMessage response data is empty"))
        } else {
            Result.success(pushMessage)
        }
    }

    fun getVideoReplys(url: String) = fire(Dispatchers.IO) {
        val videoReplies = EyeNetWork.getVideoReplies(url)
        if (videoReplies.itemList.isNullOrEmpty()) {
            Result.failure(RuntimeException("videoReplies response data is empty"))
        } else {
            Result.success(videoReplies)
        }
    }

    fun getVideoRelatedAndVideoReplies(id: Long, url: String) = fire(Dispatchers.IO) {
        val requestVideoRelatedAndVideoReplies = requestVideoRelatedAndVideoReplies(id, url)
        if (requestVideoRelatedAndVideoReplies != null) {
            Result.success(requestVideoRelatedAndVideoReplies)
        } else {
            Result.failure(RuntimeException("requestVideoRelatedAndVideoReplies response data is empty"))
        }
    }

    fun getVideoDetail(id: Long, url: String) = fire(Dispatchers.IO) {
        val requestDetail = requestDetail(id, url)
        if (requestDetail != null) {
            Result.success(requestDetail)
        } else {
            Result.failure(RuntimeException("requestDetail response data is empty"))
        }
    }

    private suspend fun requestVideoRelatedAndVideoReplies(id: Long, url: String) = withContext(Dispatchers.IO) {
        coroutineScope {
            val deferredVideoRelated = async { EyeNetWork.getVideoRelated(id) }
            val deferredVideoReplies = async { EyeNetWork.getVideoReplies(url) }
            val videoRelated = deferredVideoRelated.await()
            val videoReplies = deferredVideoReplies.await()
            val viewDetail = VideoDetail(null, videoRelated, videoReplies)
            viewDetail
        }
    }

    private suspend fun requestDetail(id: Long, url: String) = withContext(Dispatchers.IO) {
        coroutineScope {
            val deferredVideoRelated = async { EyeNetWork.getVideoRelated(id) }
            val deferredVideoReplies = async { EyeNetWork.getVideoReplies(url) }
            val deferredVideoBeanForClient = async { EyeNetWork.getVideoBeanForClient(id) }
            val videoBeanForClient = deferredVideoBeanForClient.await()
            val videoRelated = deferredVideoRelated.await()
            val videoReplies = deferredVideoReplies.await()
            val videoDetail = VideoDetail(videoBeanForClient, videoRelated, videoReplies)
            videoDetail
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