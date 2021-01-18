package com.zzz.auvapp.ui.videodetail

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.RecyclerView
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.VideoReplies
import com.zzz.auvapp.ui.common.Const
import com.zzz.auvapp.ui.common.RecyclerViewHelper
import com.zzz.auvapp.ui.common.TextCardViewHeader4ViewHolder
import com.zzz.auvapp.util.DateUtil
import com.zzz.auvapp.util.load
import com.zzz.auvapp.util.setOnClickListener
import com.zzz.common.ext.gone
import com.zzz.common.ext.inflate
import com.zzz.common.ext.toast
import com.zzz.common.ext.visible
import com.zzz.common.uitls.GlobalUtil
import com.zzz.common.uitls.dp2px

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/18
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class VideoReplyAdapter(val activity: VideoDetailActivity, val dataList: List<VideoReplies.Item>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            REPLY_BEAN_FOR_CLIENT_TYPE -> ReplyViewHolder(
                R.layout.item_new_detail_reply_type.inflate(
                    parent
                )
            )
            else -> RecyclerViewHelper.getViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is TextCardViewHeader4ViewHolder -> {
                holder.tvTitle4.text = item.data.text
                if (item.data.actionUrl != null && item.data.actionUrl.startsWith(Const.ActionUrl.REPLIES_HOT)) {
                    //热门评论
                    holder.ivInto4.visible()
                    holder.tvTitle4.layoutParams =
                        (holder.tvTitle4.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(
                                0,
                                dp2px(30f),
                                0,
                                dp2px(6f)
                            )
                        }
                    setOnClickListener(
                        holder.tvTitle4,
                        holder.ivInto4
                    ) { toast(R.string.currently_not_supported) }
                } else {
                    //相关推荐、最新评论
                    holder.tvTitle4.layoutParams =
                        (holder.tvTitle4.layoutParams as LinearLayout.LayoutParams).apply {
                            setMargins(
                                0,
                                dp2px(24f),
                                0,
                                dp2px(6f)
                            )
                        }
                    holder.ivInto4.gone()
                }
            }
            is ReplyViewHolder -> {
                holder.groupReply.gone()
                holder.ivAvatar.load(item.data.user?.avatar ?: "")
                holder.tvNickName.text = item.data.user?.nickname
                holder.tvLikeCount.text =
                    if (item.data.likeCount == 0) "" else item.data.likeCount.toString()
                holder.tvMessage.text = item.data.message
                holder.tvTime.text = getTimeReply(item.data.createTime)
                holder.ivLike.setOnClickListener { toast(R.string.currently_not_supported) }

                item.data.parentReply?.run {
                    holder.groupReply.visible()
                    holder.tvReplyUser.text =
                        String.format(GlobalUtil.getString(R.string.reply_target), user?.nickname)
                    holder.ivReplyAvatar.load(user?.avatar ?: "")
                    holder.tvReplyNickName.text = user?.nickname
                    holder.tvReplyMessage.text = message
                    holder.tvShowConversation.setOnClickListener { toast(R.string.currently_not_supported) }
                }
            }
            else -> {
                holder.itemView.gone()
            }
        }
    }

    private fun getTimeReply(dateMillis: Long): String {
        val currentMillis = System.currentTimeMillis()
        val timePast = currentMillis - dateMillis
        if (timePast > -DateUtil.MINUTE) { // 采用误差一分钟以内的算法，防止客户端和服务器时间不同步导致的显示问题
            when {
                timePast < DateUtil.DAY -> {
                    var pastHours = timePast / DateUtil.HOUR
                    if (pastHours <= 0) {
                        pastHours = 1
                    }
                    return DateUtil.getDate(dateMillis, "HH:mm")
                }
                else -> return DateUtil.getDate(dateMillis, "yyyy/MM/dd")
            }
        } else {
            return DateUtil.getDate(dateMillis, "yyyy/MM/dd HH:mm")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataList[position].type == "reply" && dataList[position].data.dataType == "ReplyBeanForClient") {
            REPLY_BEAN_FOR_CLIENT_TYPE
        } else {
            RecyclerViewHelper.getItemViewType(dataList[position])
        }
    }

    override fun getItemCount() = dataList.size

    inner class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivAvatar = view.findViewById<ImageView>(R.id.ivAvatar)
        val tvNickName = view.findViewById<TextView>(R.id.tvNickName)
        val ivLike = view.findViewById<ImageView>(R.id.ivLike)
        val tvLikeCount = view.findViewById<TextView>(R.id.tvLikeCount)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        val tvTime = view.findViewById<TextView>(R.id.tvTime)

        val groupReply = view.findViewById<Group>(R.id.groupReply)
        val tvReplyUser = view.findViewById<TextView>(R.id.tvReplyUser)
        val ivReplyAvatar = view.findViewById<ImageView>(R.id.ivReplyAvatar)
        val tvReplyNickName = view.findViewById<TextView>(R.id.tvReplyNickName)
        val tvReplyMessage = view.findViewById<TextView>(R.id.tvReplyMessage)
        val tvShowConversation = view.findViewById<TextView>(R.id.tvShowConversation)
    }

    companion object {
        const val TAG = "VideoReplyAdapter"
        const val REPLY_BEAN_FOR_CLIENT_TYPE = Const.ItemViewType.MAX
    }
}