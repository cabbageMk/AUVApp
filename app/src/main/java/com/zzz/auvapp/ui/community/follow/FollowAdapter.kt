package com.zzz.auvapp.ui.community.follow

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.Follow
import com.zzz.auvapp.ui.common.Const
import com.zzz.auvapp.ui.common.EmptyViewHolder
import com.zzz.auvapp.ui.home.recommend.HomeRecommendAdapter
import com.zzz.auvapp.ui.home.recommend.HomeRecommendAdapter.Companion.TAG
import com.zzz.auvapp.ui.videodetail.VideoDetailActivity
import com.zzz.auvapp.util.DateUtil
import com.zzz.auvapp.util.conversionVideoDuration
import com.zzz.auvapp.util.load
import com.zzz.auvapp.util.setOnClickListener
import com.zzz.common.ext.gone
import com.zzz.common.ext.inflate
import com.zzz.common.ext.toast
import com.zzz.common.ext.visible

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/07
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class FollowAdapter(val fragment: CommunityFollowFragment, val dataList: ArrayList<Follow.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Const.ItemViewType.CUSTOM_HEADER -> HeaderViewHolder(
            R.layout.item_community_follow_header_type.inflate(
                parent
            )
        )
        Const.ItemViewType.MAX -> AutoPlayFollowCardViewHolder(
            R.layout.item_community_auto_play_follow_card_follow_card_type.inflate(
                parent
            )
        )
        else -> EmptyViewHolder(View(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.itemView.setOnClickListener {
                    toast(content = "请登录")
                }
            }

            is AutoPlayFollowCardViewHolder -> {
                val item = dataList[position - 1]
                with(holder) {
                    item.data.content.data.run {
                        ivAvatar.load(item.data.header.icon ?: author?.icon ?: "")
                        tvReleaseTime.text = DateUtil.getDate(
                            releaseTime ?: author?.latestReleaseTime ?: System.currentTimeMillis(),
                            "HH:mm"
                        )
                        tvTitle.text = title
                        tvNickname.text = author?.name ?: ""
                        tvContent.text = description
                        tvCollectionCount.text = consumption.collectionCount.toString()
                        tvReplyCount.text = consumption.replyCount.toString()
                        tvVideoDuration.visible()    //视频播放后，复用tvVideoDuration直接隐藏了
                        tvVideoDuration.text = duration.conversionVideoDuration()
                        HomeRecommendAdapter.startAutoPlay(fragment.activity!!, videoPlayer, position, playUrl, cover.feed, TAG, object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                tvVideoDuration.gone()
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickResume(url: String?, vararg objects: Any?) {
                                super.onClickResume(url, *objects)
                                tvVideoDuration.gone()
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
                                holder.tvVideoDuration.visible()
                                VideoDetailActivity.start(
                                    fragment.activity!!,
                                    VideoDetailActivity.VideoInfo(id, playUrl, title, description, category, library, consumption, cover, author!!, webUrl)
                                )
                            }
                        })

                    }
                }
                setOnClickListener(
                    holder.videoPlayer.thumbImageView,
                    holder.itemView,
                    holder.ivCollectionCount,
                    holder.tvCollectionCount,
                    holder.ivFavorites,
                    holder.tvFavorites,
                    holder.ivShare
                )
                {
                    when (this) {
                        holder.videoPlayer.thumbImageView, holder.itemView -> {
//                            NewDetailActivity.start(
//                                fragment.activity, NewDetailActivity.VideoInfo(
//                                    item.data.content.data.id,
//                                    playUrl,
//                                    title,
//                                    description,
//                                    category,
//                                    library,
//                                    consumption,
//                                    cover,
//                                    author!!,
//                                    webUrl
//                                )
//                            )
                        }
                        holder.ivCollectionCount, holder.tvCollectionCount, holder.ivFavorites, holder.tvFavorites -> {
//                            LoginActivity.start(fragment.activity)
                        }
                        holder.ivShare -> {
//                            showDialogShare(fragment.activity, getShareContent(item))
                        }
                    }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }
    }

    override fun getItemCount() = dataList.size + 1

    override fun getItemViewType(position: Int) = when {
        position == 0 -> Const.ItemViewType.CUSTOM_HEADER
        dataList[position - 1].type == "autoPlayFollowCard" && dataList[position - 1].data.dataType == "FollowCard" -> Const.ItemViewType.MAX
        else -> Const.ItemViewType.UNKNOWN
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    inner class AutoPlayFollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvReleaseTime = view.findViewById<TextView>(R.id.tvReleaseTime)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvNickname = view.findViewById<TextView>(R.id.tvNickname)
        val tvContent = view.findViewById<TextView>(R.id.tvContent)
        val ivCollectionCount = view.findViewById<ImageView>(R.id.ivCollectionCount)
        val tvCollectionCount = view.findViewById<TextView>(R.id.tvCollectionCount)
        val ivReply = view.findViewById<ImageView>(R.id.ivReply)
        val tvReplyCount = view.findViewById<TextView>(R.id.tvReplyCount)
        val ivFavorites = view.findViewById<ImageView>(R.id.ivFavorites)
        val tvFavorites = view.findViewById<TextView>(R.id.tvFavorites)
        val tvVideoDuration = view.findViewById<TextView>(R.id.tvVideoDuration)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)
        val videoPlayer: GSYVideoPlayer = view.findViewById<GSYVideoPlayer>(R.id.videoPlayer)
    }
}