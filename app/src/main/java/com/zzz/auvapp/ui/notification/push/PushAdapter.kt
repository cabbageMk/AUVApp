package com.zzz.auvapp.ui.notification.push

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.PushMessage
import com.zzz.auvapp.util.ActionUrlUtil
import com.zzz.auvapp.util.DateUtil
import com.zzz.auvapp.util.load
import com.zzz.common.ext.inflate

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PushAdapter(val fragment: PushFragment, val dataList: ArrayList<PushMessage.Message>) :
    RecyclerView.Adapter<PushAdapter.PushViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PushAdapter.PushViewHolder {
        val holder = PushViewHolder(R.layout.item_notification_push.inflate(parent))
        holder.itemView.setOnClickListener {
            val item = dataList[holder.adapterPosition]
            fragment.activity?.let { it1 -> ActionUrlUtil.process(it1, item.actionUrl, item.title) }
        }
        return holder
    }

    override fun onBindViewHolder(holder: PushAdapter.PushViewHolder, position: Int) {
        with(holder) {
            val message = dataList[position]
            tvTitle.text = message.title
            tvTime.text = DateUtil.getConvertedDate(message.date)
            tvContent.text = message.content
            ivAvatar.load(message.icon) { error(R.mipmap.ic_launcher) }
        }
    }

    override fun getItemCount() = dataList.size

    inner class PushViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTitle)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
    }
}