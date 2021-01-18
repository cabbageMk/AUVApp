package com.zzz.auvapp.ui.home.recommend

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.HomePageRecommend
import com.zzz.auvapp.ui.common.*
import com.zzz.auvapp.ui.home.daily.DailyAdapter
import com.zzz.auvapp.ui.videodetail.VideoDetailActivity
import com.zzz.auvapp.util.conversionVideoDuration
import com.zzz.auvapp.util.load
import com.zzz.auvapp.util.setOnClickListener
import com.zzz.common.ext.dp2px
import com.zzz.common.ext.gone
import com.zzz.common.ext.invisible
import com.zzz.common.ext.visible
import com.zzz.common.uitls.ToastUtil
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class HomeRecommendAdapter(val context: Context, val dataList: List<HomePageRecommend.Item>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int) = RecyclerViewHelper.getItemViewType(dataList[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewHelper.getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        with(holder) {
            when (this) {
                is TextCardViewHeader5ViewHolder -> {
                    tvTitle5.text = item.data.text
                    if (item.data.actionUrl != null) ivInto5.visible() else ivInto5.gone()
                    if (item.data.follow != null) tvFollow.visible() else tvFollow.gone()
                    tvFollow.setOnClickListener {
                        // todo 跳转登录页
                    }
                    setOnClickListener(tvTitle5, ivInto5) {
                        // todo 跳转详情
                    }
                }
                is TextCardViewHeader7ViewHolder -> {
                    tvTitle7.text = item.data.text
                    tvRightText7.text = item.data.rightText
                    setOnClickListener(tvRightText7, ivInto7) {
                        // todo 跳转
//                        ActionUrlUtil.process(fragment, item.data.actionUrl, "${item.data.text},${item.data.rightText}")
                    }
                }
                is TextCardViewHeader8ViewHolder -> {
                    tvTitle8.text = item.data.text
                    tvRightText8.text = item.data.rightText
                    setOnClickListener(tvRightText8, ivInto8) {
//                        ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text)
                    }
                }
                is TextCardViewFooter2ViewHolder -> {
                    tvFooterRightText2.text = item.data.text
                    setOnClickListener(tvFooterRightText2, ivTooterInto2) {
//                        ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text)
                    }
                }
                is TextCardViewFooter3ViewHolder -> {
                    tvFooterRightText3.text = item.data.text
                    setOnClickListener(tvRefresh, tvFooterRightText3, ivTooterInto3) {
                        if (this == tvRefresh) {
//                            "${tvRefresh.text},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast()
                        } else if (this == tvFooterRightText3 || this == ivTooterInto3) {
//                            ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.text)
                        }
                    }
                }
                is FollowCardViewHolder -> {
                    ivVideo.load(item.data.content.data.cover.feed, 4f)
                    ivAvatar.load(item.data.header.icon)
                    tvVideoDuration.text = item.data.content.data.duration.conversionVideoDuration()
                    tvDescription.text = item.data.header.description
                    tvTitle.text = item.data.header.title
                    if (item.data.content.data.ad) tvLabel.visible() else tvLabel.gone()
                    if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) ivChoiceness.visible() else ivChoiceness.gone()
                    ivShare.setOnClickListener {
//                        showDialogShare(fragment.activity, "${item.data.content.data.title}：${item.data.content.data.webUrl.raw}")
                    }
                    itemView.setOnClickListener {
                        item.data.content.data.run {
                            if (ad || author == null) {
                                VideoDetailActivity.start(context as Activity, id)
                            } else {
                                VideoDetailActivity.start(
                                    context as Activity, VideoDetailActivity.VideoInfo(id, playUrl, title, description, category, library, consumption, cover, author, webUrl)
                                )
                            }
                        }
                    }
                }

                is BannerViewHolder -> {
                    ivPicture.load(item.data.image, 4f)
                    holder.itemView.setOnClickListener {
//                        ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.title)
                    }
                }
                is Banner3ViewHolder -> {
                    ivPicture.load(item.data.image, 4f)
                    ivAvatar.load(item.data.header.icon)
                    tvTitle.text = item.data.header.title
                    tvDescription.text = item.data.header.description
                    if (item.data.label?.text.isNullOrEmpty()) tvLabel.invisible() else tvLabel.visible()
                    tvLabel.text = item.data.label?.text ?: ""
                    itemView.setOnClickListener {
//                        ActionUrlUtil.process(fragment, item.data.actionUrl, item.data.header.title)
                    }
                }
                is InformationCardFollowCardViewHolder -> {
                    ivCover.load(item.data.backgroundImage, 4f, RoundedCornersTransformation.CornerType.TOP)
                    recyclerView.setHasFixedSize(true)
                    if (recyclerView.itemDecorationCount == 0) {
                        recyclerView.addItemDecoration(InformationCardFollowCardItemDecoration())
                    }
                    recyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
                    recyclerView.adapter = InformationCardFollowCardAdapter(context as Activity, item.data.actionUrl, item.data.titleList)
                    holder.itemView.setOnClickListener {
//                        ActionUrlUtil.process(fragment, item.data.actionUrl)
                    }
                }

                is VideoSmallCardViewHolder -> {
                    ivPicture.load(item.data.cover.feed, 4f)
                    tvDescription.text = if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                    tvTitle.text = item.data.title
                    tvVideoDuration.text = item.data.duration.conversionVideoDuration()
                    ivShare.setOnClickListener {
//                        showDialogShare(fragment.activity, "${item.data.title}：${item.data.webUrl.raw}")
                    }
                    itemView.setOnClickListener {
                        item.data.run {
                            VideoDetailActivity.start(
                                context as Activity, VideoDetailActivity.VideoInfo(id, playUrl, title, description, category, library, consumption, cover, author, webUrl)
                            )
                        }
                    }
                }

                is UgcSelectedCardCollectionViewHolder -> {
                    tvTitle.text = item.data.header.title
                    tvRightText.text = item.data.header.rightText
                    tvRightText.setOnClickListener {
//                        EventBus.getDefault().post(SwitchPagesEvent(com.eyepetizer.android.ui.community.commend.CommendFragment::class.java))
//                        EventBus.getDefault().post(RefreshEvent(CommunityFragment::class.java))
                    }
                    item.data.itemList.forEachIndexed { index, it ->
                        when (index) {
                            0 -> {
                                ivCoverLeft.load(it.data.url, 4f, RoundedCornersTransformation.CornerType.LEFT)
                                if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) ivLayersLeft.visible()
                                ivAvatarLeft.load(it.data.userCover)
                                tvNicknameLeft.text = it.data.nickname
                            }
                            1 -> {
                                ivCoverRightTop.load(it.data.url, 4f, RoundedCornersTransformation.CornerType.TOP_RIGHT)
                                if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) ivLayersRightTop.visible()
                                ivAvatarRightTop.load(it.data.userCover)
                                tvNicknameRightTop.text = it.data.nickname
                            }
                            2 -> {
                                ivCoverRightBottom.load(it.data.url, 4f, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT)
                                if (!it.data.urls.isNullOrEmpty() && it.data.urls.size > 1) ivLayersRightBottom.visible()
                                ivAvatarRightBottom.load(it.data.userCover)
                                tvNicknameRightBottom.text = it.data.nickname
                            }
                        }
                    }
                    holder.itemView.setOnClickListener { ToastUtil.show(R.string.currently_not_supported) }
                }

                is TagBriefCardViewHolder -> {
                    ivPicture.load(item.data.icon, 4f)
                    tvDescription.text = item.data.description
                    tvTitle.text = item.data.title
                    if (item.data.follow != null) tvFollow.visible() else tvFollow.gone()
                    tvFollow.setOnClickListener {
//                        LoginActivity.start(fragment.activity)
                    }
                    itemView.setOnClickListener {
//                        "${item.data.title},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast()
                    }
                }
                is TopicBriefCardViewHolder -> {
                    ivPicture.load(item.data.icon, 4f)
                    tvDescription.text = item.data.description
                    tvTitle.text = item.data.title
                    holder.itemView.setOnClickListener {
//                        "${item.data.title},${GlobalUtil.getString(R.string.currently_not_supported)}".showToast()
                    }
                }

                is AutoPlayVideoAdViewHolder -> {
                    item.data.detail?.run {
                        ivAvatar.load(item.data.detail.icon)
                        tvTitle.text = item.data.detail.title
                        tvDescription.text = item.data.detail.description
                        startAutoPlay(context as Activity, videoPlayer, position, url, imageUrl, TAG, object : GSYSampleCallBack() {
                            override fun onPrepared(url: String?, vararg objects: Any?) {
                                super.onPrepared(url, *objects)
                                GSYVideoManager.instance().isNeedMute = true
                            }

                            override fun onClickBlank(url: String?, vararg objects: Any?) {
                                super.onClickBlank(url, *objects)
//                                ActionUrlUtil.process(fragment, item.data.detail.actionUrl)
                            }
                        })
                        setOnClickListener(videoPlayer.thumbImageView, holder.itemView) {
//                            ActionUrlUtil.process(fragment, item.data.detail.actionUrl)
                        }
                    }
                }
                else -> {
                    holder.itemView.gone()
//                    if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
                }
            }
        }
    }

    companion object {
        const val TAG = "HomeRecommendAdapter"

        fun startAutoPlay(activity: Activity, player: GSYVideoPlayer, position: Int, playUrl: String, coverUrl: String, playTag: String, callBack: GSYSampleCallBack? = null) {
            player.run {
                //防止错位设置
                setPlayTag(playTag)
                //设置播放位置防止错位
                setPlayPosition(position)
                //音频焦点冲突时是否释放
                setReleaseWhenLossAudio(false)
                //设置循环播放
                setLooping(true)
                //增加封面
                val cover = ImageView(activity)
                cover.scaleType = ImageView.ScaleType.CENTER_CROP
                cover.load(coverUrl, 4f)
                cover.parent?.run { removeView(cover) }
                setThumbImageView(cover)
                //设置播放过程中的回调
                setVideoAllCallBack(callBack)
                //设置播放URL
                setUp(playUrl, false, null)
            }
        }
    }

    class InformationCardFollowCardAdapter(val activity: Activity, val actionUrl: String?, val dataList: List<String>) :
        RecyclerView.Adapter<InformationCardFollowCardAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNews = view.findViewById<TextView>(R.id.tvNews)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InformationCardFollowCardAdapter.ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_information_card_type_item, parent, false))
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(holder: InformationCardFollowCardAdapter.ViewHolder, position: Int) {
            val item = dataList[position]
            holder.tvNews.text = item
            holder.itemView.setOnClickListener {
//                ActionUrlUtil.process(activity, actionUrl)
            }
        }
    }

    class InformationCardFollowCardItemDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view) // item position
            val count = parent.adapter?.itemCount //item count
            count?.run {
                when (position) {
                    0 -> {
                        outRect.top = dp2px(18f)
                    }
                    count.minus(1) -> {
                        outRect.top = dp2px(13f)
                        outRect.bottom = dp2px(18f)
                    }
                    else -> {
                        outRect.top = dp2px(13f)
                    }
                }
            }
        }
    }

}