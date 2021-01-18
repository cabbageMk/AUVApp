package com.zzz.auvapp.ui.videodetail

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.VideoRelated
import com.zzz.auvapp.ui.common.Const
import com.zzz.auvapp.ui.common.RecyclerViewHelper
import com.zzz.auvapp.ui.common.TextCardViewHeader4ViewHolder
import com.zzz.auvapp.ui.common.VideoSmallCardViewHolder
import com.zzz.auvapp.ui.home.daily.DailyAdapter
import com.zzz.auvapp.util.conversionVideoDuration
import com.zzz.auvapp.util.load
import com.zzz.common.ext.gone
import com.zzz.common.ext.inflate
import com.zzz.common.ext.toast
import com.zzz.common.ext.visible

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/18
 *     desc   : 视频相关推荐适配器
 *     version: 1.0
 * </pre>
 */
class VideoRelatedAdapter(
    private val activity: VideoDetailActivity,
    val dataList: List<VideoRelated.Item>,
    private var videoInfoData: VideoDetailActivity.VideoInfo?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            Const.ItemViewType.CUSTOM_HEADER
        } else if (dataList[position - 1].type == "simpleHotReplyScrollCard" && dataList[position - 1].data.dataType == "ItemCollection") {
            SIMPLE_HOT_REPLY_CARD_TYPE
        } else {
            RecyclerViewHelper.getItemViewType(dataList[position - 1])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Const.ItemViewType.CUSTOM_HEADER -> CustomHeaderViewHolder(R.layout.item_new_detail_custom_header_type.inflate(parent))
        SIMPLE_HOT_REPLY_CARD_TYPE -> SimpleHotReplyCardViewHolder(View(parent.context))
        else -> RecyclerViewHelper.getViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is CustomHeaderViewHolder -> {
                videoInfoData?.let {
                    with(holder) {
                        groupAuthor.gone()
                        tvTitle.text = videoInfoData?.title
                        tvCategory.text =
                            if (videoInfoData?.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${videoInfoData?.category} / 开眼精选" else "#${videoInfoData?.category}"
                        tvDescription.text = videoInfoData?.description
                        tvCollectionCount.text = videoInfoData?.consumption?.collectionCount.toString()
                        tvShareCount.text = videoInfoData?.consumption?.shareCount.toString()
                        videoInfoData?.author?.run {
                            groupAuthor.visible()
                            ivAvatar.load(videoInfoData?.author?.icon ?: "")
                            tvAuthorDescription.text = videoInfoData?.author?.description
                            tvAuthorName.text = videoInfoData?.author?.name
                        }
                    }
                }

            }
            is SimpleHotReplyCardViewHolder -> {

            }
            is TextCardViewHeader4ViewHolder -> {
                val item = dataList[position - 1]
                holder.tvTitle4.text = item.data.text
            }
            is VideoSmallCardViewHolder -> {
                val item = dataList[position - 1]
                holder.ivPicture.load(item.data.cover.feed, 4f)
                holder.tvDescription.text = if (item.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) "#${item.data.category} / 开眼精选" else "#${item.data.category}"
                holder.tvDescription.setTextColor(ContextCompat.getColor(activity, R.color.whiteAlpha35))
                holder.tvTitle.text = item.data.title
                holder.tvTitle.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.tvVideoDuration.text = item.data.duration.conversionVideoDuration()
                holder.ivShare.setOnClickListener { toast("分享") }
                holder.itemView.setOnClickListener {
                    item.data.run {
                        VideoDetailActivity.start(activity, VideoDetailActivity.VideoInfo(id, playUrl, title, description, category, library, consumption, cover, author, webUrl))
                        activity.scrollTop()
                    }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }
    }

    override fun getItemCount() = dataList.size + 1
    fun bindVideoInfo(videoInfoData: VideoDetailActivity.VideoInfo?) {
        this.videoInfoData = videoInfoData
    }

    inner class SimpleHotReplyCardViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class CustomHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvCategory = view.findViewById<TextView>(R.id.tvCategory)
        val ivFoldText = view.findViewById<ImageView>(R.id.ivFoldText)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val ivCollectionCount = view.findViewById<ImageView>(R.id.ivCollectionCount)
        val tvCollectionCount = view.findViewById<TextView>(R.id.tvCollectionCount)
        val ivShare = view.findViewById<ImageView>(R.id.ivShare)
        val tvShareCount = view.findViewById<TextView>(R.id.tvShareCount)
        val ivCache = view.findViewById<ImageView>(R.id.ivCache)
        val tvCache = view.findViewById<TextView>(R.id.tvCache)
        val ivFavorites = view.findViewById<ImageView>(R.id.ivFavorites)
        val tvFavorites = view.findViewById<TextView>(R.id.tvFavorites)
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvAuthorDescription = view.findViewById<TextView>(R.id.tvAuthorDescription)
        val tvAuthorName = view.findViewById<TextView>(R.id.tvAuthorName)
        val tvFollow = view.findViewById<TextView>(R.id.tvFollow)
        val groupAuthor = view.findViewById<Group>(R.id.groupAuthor)
    }

    companion object {
        const val TAG = "NewDetailRelatedAdapter"
        const val SIMPLE_HOT_REPLY_CARD_TYPE = Const.ItemViewType.MAX
    }
}