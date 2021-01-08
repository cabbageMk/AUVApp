package com.zzz.auvapp.ui.community.follow

import android.app.AlertDialog
import android.content.Context
import android.graphics.Rect
import android.net.ConnectivityManager
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.gsyvideoplayer.R
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.zzz.common.uitls.dp2px
import com.zzz.common.uitls.screenHeight

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class FollowScrollListener(private val itemPlayId: Int) : RecyclerView.OnScrollListener() {

    companion object {
        /**
         * 指定自动播放，在屏幕上的区域范围，上下
         */
        val LIMIT_TOP = screenHeight / 2 - dp2px(180f)
        val LIMIT_BOTTOM = screenHeight / 2 + dp2px(180f)
    }

    private var firstVisible: Int? = null
    private var lastVisible: Int? = null
    private var visibleCount: Int = 0

    private var runnable: PlayRunnable? = null
    private val playHandler = Handler(Looper.myLooper()!!)

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            playVideo(recyclerView)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val firstVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastVisibleItem =
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        // 保存当前可见范围
        if (firstVisible == firstVisibleItem) return
        firstVisible = firstVisibleItem
        lastVisible = lastVisibleItem
        visibleCount = lastVisibleItem - firstVisibleItem
    }

    private fun playVideo(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        var gsyBaseVideoPlayer: GSYBaseVideoPlayer? = null
        var needPlay = false
        for (i in 0 until visibleCount) {
            val childAt = layoutManager!!.getChildAt(i) ?: continue
            val player = childAt.findViewById<GSYBaseVideoPlayer>(itemPlayId) ?: continue
            val rect = Rect()
            player.getLocalVisibleRect(rect)
            val height = player.height
            //说明第一个完全可视
            if (rect.top == 0 && rect.bottom == height) {
                gsyBaseVideoPlayer = player
                if (player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_NORMAL || player.currentPlayer.currentState == GSYBaseVideoPlayer.CURRENT_STATE_ERROR) {
                    needPlay = true
                }
                break
            }
        }
        if (gsyBaseVideoPlayer != null && needPlay) {
            runnable?.let {
                val tmpPlayer = it.videoPlayer
                playHandler.removeCallbacks(it)
                runnable = null
                if (tmpPlayer === gsyBaseVideoPlayer) return
            }
            runnable = PlayRunnable(gsyBaseVideoPlayer)
            playHandler.postDelayed(runnable!!, 400)
        }
    }


    private inner class PlayRunnable(var videoPlayer: GSYBaseVideoPlayer) : Runnable {
        override fun run() {
            // 判断是否在屏幕中可播放范围
            var inPosition = false
            val screenPosition = IntArray(2)
            videoPlayer.getLocationOnScreen(screenPosition)
            val halfHeight = videoPlayer.height / 2
            val rangePosition = screenPosition[1] + halfHeight
            if (rangePosition in LIMIT_TOP..LIMIT_BOTTOM) {
                inPosition = true
            }
            if (inPosition) {
                // 开始播放
                startPlay(videoPlayer, videoPlayer.context)
            }
        }
    }

    private var showTip = true

    private fun startPlay(videoPlayer: GSYBaseVideoPlayer, context: Context?) {
        context?.let {
            if (!isWifiConnected(it) && showTip) {
                showDialog(it, videoPlayer)
                return
            }
        }
        videoPlayer.startPlayLogic()
    }

    private fun showDialog(context: Context, videoPlayer: GSYBaseVideoPlayer) {
        AlertDialog.Builder(context).apply {
            setMessage(context.resources.getString(R.string.tips_not_wifi))
            setPositiveButton(context.resources.getString(R.string.tips_not_wifi_confirm)) { dialog, which ->
                dialog.dismiss()
                videoPlayer.startPlayLogic()
                showTip = false
            }
            setNegativeButton(context.resources.getString(R.string.tips_not_wifi_cancel)) { dialog, which ->
                dialog.dismiss()
                showTip = false
            }
            create()
        }.show()
    }

    fun isWifiConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return wifiNetworkInfo!!.isConnected
    }


}