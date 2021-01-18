package com.zzz.auvapp.ui.videodetail

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.MergeAdapter
import com.shuyu.gsyvideoplayer.GSYVideoADManager
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.ActivityVideoDetailBinding
import com.zzz.auvapp.logic.model.Author
import com.zzz.auvapp.logic.model.Consumption
import com.zzz.auvapp.logic.model.Cover
import com.zzz.auvapp.logic.model.WebUrl
import com.zzz.auvapp.util.load
import com.zzz.common.ext.*
import com.zzz.common.mvvm.BaseVMActivity
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_video_detail.*
import kotlinx.coroutines.*

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class VideoDetailActivity : BaseVMActivity() {
    private val binding by binding<ActivityVideoDetailBinding>(R.layout.activity_video_detail)
    private val viewModel by lazy { ViewModelProvider(this).get(VideoDetailViewModel::class.java) }
    private var orientationUtils: OrientationUtils? = null
    private lateinit var relatedAdapter: VideoRelatedAdapter
    private lateinit var replyAdapter: VideoReplyAdapter
    private lateinit var mergeAdapter: MergeAdapter

    private val globalJob by lazy { Job() }
    private var hideTitleBarJob: Job? = null
    private var hideBottomContainerJob: Job? = null

    override fun openObserve() {
        if (!viewModel.videoDetailLiveData.hasObservers()) {
            viewModel.videoDetailLiveData.observe(this) {result ->
                val response = result.getOrNull() ?: return@observe
                viewModel.nextPageUrl = response.videoReplies.nextPageUrl
                if (response.videoRelated == null || response.videoRelated.itemList.isNullOrEmpty() && response.videoReplies.itemList.isNullOrEmpty()) {
                    return@observe
                }
                response.videoBeanForClient?.run {
                    viewModel.videoInfoData = VideoInfo(id, playUrl, title, description, category, library, consumption, cover, author, webUrl)
                    startVideoPlay()
                    relatedAdapter.bindVideoInfo(viewModel.videoInfoData)
                }
                viewModel.relatedDataList.clear()
                viewModel.repliesDataList.clear()
                viewModel.relatedDataList.addAll(response.videoRelated.itemList)
                viewModel.repliesDataList.addAll(response.videoReplies.itemList)
                relatedAdapter.notifyDataSetChanged()
                replyAdapter.notifyDataSetChanged()
                when {
                    viewModel.repliesDataList.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                    response.videoReplies.nextPageUrl.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                    else -> refreshLayout.closeHeaderOrFooter()
                }
            }
        }
        if (!viewModel.repliesAndRepliesLiveData.hasObservers()) {
            viewModel.repliesAndRepliesLiveData.observe(this) {result->
                val response = result.getOrNull() ?: return@observe
                viewModel.nextPageUrl = response.videoReplies.nextPageUrl
                if (response.videoRelated == null || response.videoRelated.itemList.isNullOrEmpty() && response.videoReplies.itemList.isNullOrEmpty()) {
                    return@observe
                }
                viewModel.relatedDataList.clear()
                viewModel.repliesDataList.clear()
                viewModel.relatedDataList.addAll(response.videoRelated.itemList)
                viewModel.repliesDataList.addAll(response.videoReplies.itemList)
                relatedAdapter.bindVideoInfo(viewModel.videoInfoData)
                relatedAdapter.notifyDataSetChanged()
                replyAdapter.notifyDataSetChanged()
                when {
                    viewModel.repliesDataList.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                    response.videoReplies.nextPageUrl.isNullOrEmpty() -> refreshLayout.finishLoadMoreWithNoMoreData()
                    else -> refreshLayout.closeHeaderOrFooter()
                }
            }
        }
        if (!viewModel.repliesLiveData.hasObservers()) {
            viewModel.repliesLiveData.observe(this) {result->
                val response = result.getOrNull() ?: return@observe
                viewModel.nextPageUrl = response.nextPageUrl
                if (response.itemList.isNullOrEmpty()) {
                    return@observe
                }
                val itemCount = replyAdapter.itemCount
                viewModel.repliesDataList.addAll(response.itemList)
                replyAdapter.notifyItemRangeInserted(itemCount, response.itemList.size)
                if (response.nextPageUrl.isNullOrEmpty()) {
                    refreshLayout.finishLoadMoreWithNoMoreData()
                } else {
                    refreshLayout.closeHeaderOrFooter()
                }
            }
        }
    }

    override fun initView() {
        setStatusBarBackground(R.color.black)
        if (checkIntentData()) finish()
        getIntentData()

        orientationUtils = OrientationUtils(this, binding.videoPlayer)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        relatedAdapter =
            VideoRelatedAdapter(this, viewModel.relatedDataList, viewModel.videoInfoData)
        replyAdapter = VideoReplyAdapter(this, viewModel.repliesDataList)
        mergeAdapter = MergeAdapter(relatedAdapter, replyAdapter)
        binding.recyclerView.adapter = mergeAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.itemAnimator = null

        binding.refreshLayout.run {
            setDragRate(0.7f)
            setHeaderTriggerRate(0.6f)
            setFooterTriggerRate(0.6f)
            setEnableLoadMoreWhenContentNotFull(true)
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableFooterTranslationContent(true)
            setEnableScrollContentWhenLoaded(true)
            setEnableNestedScroll(true)
            setFooterHeight(153f)
            setOnRefreshListener { finish() }
            setOnLoadMoreListener { viewModel.loadMore() }
        }

        startVideoPlay()

    }

    private fun getIntentData() {
        if (intent.getParcelableExtra<VideoInfo>(EXTRA_VIDEOINFO) != null) viewModel.videoInfoData =
            intent.getParcelableExtra(EXTRA_VIDEOINFO)
        if (intent.getLongExtra(EXTRA_VIDEO_ID, 0L) != 0L) viewModel.videoId =
            intent.getLongExtra(EXTRA_VIDEO_ID, 0L)
    }

    private fun startVideoPlay() {
        viewModel.videoInfoData?.run {
            binding.ivBlurredBg.load(cover.blurred)
            binding.tvReplyCount.text = consumption.replyCount.toString()
            binding.videoPlayer.startPlay()
        }
    }

    override fun initData() {
        viewModel.onRefresh()
    }

    private fun GSYVideoPlayer.startPlay() {
        viewModel.videoInfoData?.let {
            //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
            fullscreenButton.setOnClickListener { showFull() }
            //防止错位设置
            playTag = TAG
            //音频焦点冲突时是否释放
            isReleaseWhenLossAudio = false
            //增加封面
            val imageView = ImageView(this@VideoDetailActivity)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.load(it.cover.detail)
            thumbImageView = imageView
            thumbImageView.setOnClickListener { switchTitleBarVisible() }
            //是否开启自动旋转
            isRotateViewAuto = false
            //是否需要全屏锁定屏幕功能
            isNeedLockFull = true
            //是否可以滑动调整
            setIsTouchWiget(true)
            //设置触摸显示控制ui的消失时间
            dismissControlTime = 5000
            //设置播放过程中的回调
            setVideoAllCallBack(VideoCallPlayBack())
            //设置播放URL
            setUp(it.playUrl, false, it.title)
            //开始播放
            startPlayLogic()
        }
    }

    private fun switchTitleBarVisible() {
        if (binding.videoPlayer.currentPlayer.currentState == GSYVideoView.CURRENT_STATE_AUTO_COMPLETE) return
        if (binding.flHeader.visibility == View.VISIBLE) {
            hideTitleBar()
        } else {
            binding.flHeader.visibleAlphaAnimation(1000)
            binding.ivPullDown.visibleAlphaAnimation(1000)
            binding.ivCollection.visibleAlphaAnimation(1000)
            binding.ivMore.visibleAlphaAnimation(1000)
            binding.ivShare.visibleAlphaAnimation(1000)
            delayHideTitleBar()
        }
    }

    private fun hideTitleBar() {
        binding.flHeader.invisibleAlphaAnimation(1000)
        binding.ivPullDown.goneAlphaAnimation(1000)
        binding.ivCollection.goneAlphaAnimation(1000)
        binding.ivMore.goneAlphaAnimation(1000)
        binding.ivShare.goneAlphaAnimation(1000)
    }

    private fun delayHideTitleBar() {
        hideTitleBarJob?.cancel()
        hideTitleBarJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(binding.videoPlayer.dismissControlTime.toLong())
            hideTitleBar()
        }
    }

    private fun delayHideBottomContainer() {
        hideBottomContainerJob?.cancel()
        hideBottomContainerJob = CoroutineScope(globalJob).launch(Dispatchers.Main) {
            delay(binding.videoPlayer.dismissControlTime.toLong())
            binding.videoPlayer.getBottomContainer().gone()
            binding.videoPlayer.startButton.gone()
        }
    }

    private fun showFull() {
        orientationUtils?.run { if (isLand != 1) resolveByClick() }
        binding.videoPlayer.startWindowFullscreen(this, true, false)
    }

    private fun checkIntentData() =
        intent.getParcelableExtra<VideoInfo>(EXTRA_VIDEOINFO) == null && intent.getLongExtra(
            EXTRA_VIDEO_ID,
            0L
        ) == 0L

    fun scrollTop() {
        if (relatedAdapter.itemCount != 0) {
            binding.recyclerView.scrollToPosition(0)
            refreshLayout.invisibleAlphaAnimation(2500)
            refreshLayout.visibleAlphaAnimation(1500)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (checkIntentData()) {
            getIntentData()
            startVideoPlay()
            viewModel.onRefresh()
        }
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer.onVideoResume()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        videoPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true)
    }

    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) return
        super.onBackPressed()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.anl_push_bottom_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoADManager.releaseAllVideos()
        orientationUtils?.releaseListener()
        videoPlayer.release()
        videoPlayer.setVideoAllCallBack(null)
        globalJob.cancel()
    }

    inner class VideoCallPlayBack : GSYSampleCallBack() {
        override fun onStartPrepared(url: String?, vararg objects: Any?) {
            super.onStartPrepared(url, *objects)
            binding.flHeader.gone()
            binding.llShares.gone()
        }

        override fun onClickBlank(url: String?, vararg objects: Any?) {
            super.onClickBlank(url, *objects)
            switchTitleBarVisible()
        }

        override fun onClickStop(url: String?, vararg objects: Any?) {
            super.onClickStop(url, *objects)
            delayHideBottomContainer()
        }

        override fun onAutoComplete(url: String?, vararg objects: Any?) {
            super.onAutoComplete(url, *objects)
            binding.flHeader.visible()
            binding.ivPullDown.visible()
            binding.ivCollection.gone()
            binding.ivShare.gone()
            binding.ivMore.gone()
            binding.llShares.visible()
        }
    }

    @Parcelize
    data class VideoInfo(
        val videoId: Long,
        val playUrl: String,
        val title: String,
        val description: String,
        val category: String,
        val library: String,
        val consumption: Consumption,
        val cover: Cover,
        val author: Author?,
        val webUrl: WebUrl
    ) : Parcelable

    companion object {
        const val TAG = "NewDetailActivity"

        const val EXTRA_VIDEOINFO = "videoInfo"
        const val EXTRA_VIDEO_ID = "videoId"

        fun start(context: Activity, videoInfo: VideoInfo) {
            val starter = Intent(context, VideoDetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEOINFO, videoInfo)
            context.startActivity(starter)
            context.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }

        fun start(context: Activity, videoId: Long) {
            val starter = Intent(context, VideoDetailActivity::class.java)
            starter.putExtra(EXTRA_VIDEO_ID, videoId)
            context.startActivity(starter)
            context.overridePendingTransition(R.anim.anl_push_bottom_in, R.anim.anl_push_up_out)
        }
    }
}