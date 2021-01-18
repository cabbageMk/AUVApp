package com.zzz.auvapp.ui.videodetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.zzz.auvapp.logic.Repository
import com.zzz.auvapp.logic.model.VideoRelated
import com.zzz.auvapp.logic.model.VideoReplies
import com.zzz.auvapp.logic.network.EyeService
import com.zzz.common.mvvm.BaseViewModel


/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class VideoDetailViewModel : BaseViewModel() {

    var relatedDataList = ArrayList<VideoRelated.Item>()

    var repliesDataList = ArrayList<VideoReplies.Item>()

    var videoInfoData: VideoDetailActivity.VideoInfo? = null

    var videoId: Long = 0L

    var nextPageUrl: String? = null

    private var repliesLiveData_ = MutableLiveData<String>()
    private var videoDetailLiveData_ = MutableLiveData<RequestParam>()
    private var repliesAndRepliesLiveData_ = MutableLiveData<RequestParam>()

    inner class RequestParam(val videoId: Long, val repliesUrl: String)

    // 视频信息+相关推荐+评论
    val videoDetailLiveData = Transformations.switchMap(videoDetailLiveData_) {
        Repository.getVideoDetail(it.videoId, it.repliesUrl)
    }
    // 相关推荐+评论
    val repliesAndRepliesLiveData = Transformations.switchMap(repliesAndRepliesLiveData_) {
        Repository.getVideoRelatedAndVideoReplies(it.videoId, it.repliesUrl)
    }
    // 评论
    val repliesLiveData = Transformations.switchMap(repliesLiveData_) {
        Repository.getVideoReplys(it)
    }

    fun onRefresh() {
        if (videoInfoData == null) {
            videoDetailLiveData_.value =
                RequestParam(videoId, "${EyeService.VIDEO_REPLIES_URL}$videoId")
        } else {
            repliesAndRepliesLiveData_.value = RequestParam(
                videoInfoData?.videoId ?: 0L,
                "${EyeService.VIDEO_REPLIES_URL}${videoInfoData?.videoId ?: 0L}"
            )
        }
    }

    fun loadMore() {
        repliesLiveData_.value = nextPageUrl
    }

}