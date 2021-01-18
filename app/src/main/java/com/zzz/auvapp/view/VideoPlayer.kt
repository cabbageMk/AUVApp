package com.zzz.auvapp.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView
import com.zzz.auvapp.R
import com.zzz.common.ext.gone
import com.zzz.common.ext.logd

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/15
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class VideoPlayer: StandardGSYVideoPlayer {

    /**
     *  是否第一次加载视频。用于隐藏进度条、播放按钮等UI。播放完成后，重新加载视频，会重置为true。
     */
    private var initFirstLoad = true

    constructor(context: Context?) : super(context)
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun getLayoutId(): Int {
        return R.layout.layout_video_player
    }

    override fun updateStartImage() {
        if (mStartButton is ImageView) {
            val imgStart = mStartButton as ImageView
            when(mCurrentState) {
                GSYVideoView.CURRENT_STATE_PLAYING -> {
                    imgStart.setImageResource(R.drawable.ic_pause_white_24dp)
                    imgStart.setBackgroundResource(R.drawable.sel_pause_white_bg)
                }
                GSYVideoView.CURRENT_STATE_ERROR -> {
                    imgStart.setImageResource(R.drawable.ic_play_white_24dp)
                    imgStart.setBackgroundResource(R.drawable.sel_play_white_bg)
                }
                GSYVideoView.CURRENT_STATE_AUTO_COMPLETE -> {
                    imgStart.setImageResource(R.drawable.ic_refresh_white_24dp)
                    imgStart.setBackgroundResource(0)
                }
                else -> {
                    imgStart.setImageResource(R.drawable.ic_play_white_24dp)
                    imgStart.setBackgroundResource(R.drawable.sel_play_white_bg)
                }
            }
        } else {
            super.updateStartImage()
        }
    }

    //正常
    override fun changeUiToNormal() {
        super.changeUiToNormal()
        ("changeUiToNormal").logd(javaClass.simpleName)
        initFirstLoad = true
    }

    //准备中
    override fun changeUiToPreparingShow() {
        super.changeUiToPreparingShow()
        ("changeUiToPreparingShow").logd(javaClass.simpleName)
        mBottomContainer.gone()
        mStartButton.gone()
    }

    //播放中
    override fun changeUiToPlayingShow() {
        super.changeUiToPlayingShow()
        ("changeUiToPlayingShow").logd(javaClass.simpleName)
        if (initFirstLoad) {
            mBottomContainer.gone()
            mStartButton.gone()
        }
        initFirstLoad = false
    }

    //开始缓冲
    override fun changeUiToPlayingBufferingShow() {
        super.changeUiToPlayingBufferingShow()
        ("changeUiToPlayingBufferingShow").logd(javaClass.simpleName)
    }

    //暂停
    override fun changeUiToPauseShow() {
        super.changeUiToPauseShow()
        ("changeUiToPauseShow").logd(javaClass.simpleName)
    }

    //自动播放结束
    override fun changeUiToCompleteShow() {
        super.changeUiToCompleteShow()
        ("changeUiToCompleteShow").logd(javaClass.simpleName)
        mBottomContainer.gone()
    }

    //错误状态
    override fun changeUiToError() {
        super.changeUiToError()
        ("changeUiToError").logd(javaClass.simpleName)
    }

    fun getBottomContainer() = mBottomContainer
}