package com.zzz.auvapp.ui.community.recommend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.youth.banner.Banner
import com.zzz.auvapp.R
import com.zzz.auvapp.logic.model.CommunityRecommend
import com.zzz.auvapp.logic.model.HomePageDiscovery
import com.zzz.auvapp.ui.common.Const
import com.zzz.auvapp.ui.common.EmptyViewHolder
import com.zzz.auvapp.ui.home.daily.DailyAdapter
import com.zzz.auvapp.ui.home.discovery.MyBannerAdapter
import com.zzz.auvapp.util.load
import com.zzz.auvapp.util.setDrawable
import com.zzz.common.ext.gone
import com.zzz.common.ext.inflate
import com.zzz.common.ext.invisible
import com.zzz.common.ext.visible
import com.zzz.common.uitls.dp2px
import com.zzz.common.uitls.screenWidth
import de.hdodenhof.circleimageview.CircleImageView

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class CommunityRecommendAdapter(
    val fragment: CommunityRecommendFragment,
    val dataList: ArrayList<CommunityRecommend.Item>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount() = dataList.size

    override fun getItemViewType(position: Int): Int {
        val item = dataList[position]
        return when (item.type) {
            STR_HORIZONTAL_SCROLLCARD_TYPE -> {
                when (item.data.dataType) {
                    STR_ITEM_COLLECTION_DATA_TYPE -> HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE
                    STR_HORIZONTAL_SCROLLCARD_DATA_TYPE -> HORIZONTAL_SCROLLCARD_TYPE
                    else -> Const.ItemViewType.UNKNOWN
                }
            }
            STR_COMMUNITY_COLUMNS_CARD -> {
                if (item.data.dataType == STR_FOLLOW_CARD_DATA_TYPE) FOLLOW_CARD_TYPE
                else Const.ItemViewType.UNKNOWN
            }
            else -> Const.ItemViewType.UNKNOWN
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE ->
                HorizontalScrollcardItemCollectionViewHolder(
                    R.layout.item_community_horizontal_scrollcard_item_collection_type.inflate(
                        parent
                    )
                )
            HORIZONTAL_SCROLLCARD_TYPE ->
                HorizontalScrollcardViewHolder(
                    R.layout.item_community_horizontal_scrollcard_type.inflate(
                        parent
                    )
                )
            FOLLOW_CARD_TYPE ->
                FollowCardViewHolder(
                    R.layout.item_community_columns_card_follow_card_type.inflate(
                        parent
                    )
                )
            else ->
                EmptyViewHolder(View(parent.context))
        }
    }

    class FollowCardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBgPicture: ImageView = view.findViewById(R.id.ivBgPicture)
        val tvChoiceness: TextView = view.findViewById(R.id.tvChoiceness)
        val ivLayers: ImageView = view.findViewById(R.id.ivLayers)
        val ivPlay: ImageView = view.findViewById(R.id.ivPlay)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val ivAvatar: ImageView = view.findViewById(R.id.ivAvatar)
        val ivRoundAvatar: CircleImageView = view.findViewById(R.id.ivRoundAvatar)
        val tvNickName: TextView = view.findViewById(R.id.tvNickName)
        val tvCollectionCount: TextView = view.findViewById(R.id.tvCollectionCount)
    }

    class HorizontalScrollcardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bannerViewPager: Banner<HomePageDiscovery.ItemX, MyBannerAdapter> =
            view.findViewById(R.id.bannerViewPager)
    }

    class HorizontalScrollcardItemCollectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataList[position]
        when (holder) {
            is HorizontalScrollcardItemCollectionViewHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
                holder.recyclerView.layoutManager = LinearLayoutManager(fragment.activity).apply {
                    orientation = LinearLayoutManager.HORIZONTAL
                }
                if (holder.recyclerView.itemDecorationCount == 0) {
                    holder.recyclerView.addItemDecoration(
                        SquareCardOfCommunityContentItemDecoration()
                    )
                }
                holder.recyclerView.adapter =
                    SquareCardOfCommunityContentAdapter(fragment, item.data.itemList)
            }

            is HorizontalScrollcardViewHolder -> {
                (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
                holder.bannerViewPager.run {
                    addBannerLifecycleObserver(fragment)
                    isAutoLoop(true)
                    setBannerRound(dp2px(4f).toFloat())
                    setAdapter(MyBannerAdapter(item.data.itemList), true)
                }
            }

            is FollowCardViewHolder -> {
                holder.tvChoiceness.gone()
                holder.ivPlay.gone()
                holder.ivLayers.gone()

                if (item.data.content.data.library == DailyAdapter.DAILY_LIBRARY_TYPE) holder.tvChoiceness.visible()

                if (item.data.header?.iconType ?: "".trim() == "round") {
                    holder.ivAvatar.invisible()
                    holder.ivRoundAvatar.visible()
                    holder.ivRoundAvatar.load(item.data.content.data.owner.avatar)
                } else {
                    holder.ivRoundAvatar.gone()
                    holder.ivAvatar.visible()
                    holder.ivAvatar.load(item.data.content.data.owner.avatar)
                }

                holder.ivBgPicture.run {
                    val imageHeight = calculateImageHeight(item.data.content.data.width, item.data.content.data.height)
                    layoutParams.width = mMaxWidth
                    layoutParams.height = imageHeight
                    this.load(item.data.content.data.cover.feed, 4f)
                }
                holder.tvCollectionCount.text =
                    item.data.content.data.consumption.collectionCount.toString()
                val drawable = ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_favorite_border_black_20dp
                )
                holder.tvCollectionCount.setDrawable(drawable, 17f, 17f, 2)
                holder.tvDescription.text = item.data.content.data.description
                holder.tvNickName.text = item.data.content.data.owner.nickname

                when (item.data.content.type) {
                    STR_VIDEO_TYPE -> {
                        holder.ivPlay.visible()
                        holder.itemView.setOnClickListener {
//                            val items = dataList.filter { it.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
//                            UgcDetailActivity.start(fragment.activity, items, item)
                        }
                    }
                    STR_UGC_PICTURE_TYPE -> {
                        if (!item.data.content.data.urls.isNullOrEmpty() && item.data.content.data.urls.size > 1) holder.ivLayers.visible()
                        holder.itemView.setOnClickListener {
//                            val items = dataList.filter { it.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
//                            UgcDetailActivity.start(fragment.activity, items, item)
                        }
                    }
                    else -> {

                    }
                }
            }
            else -> {
                holder.itemView.gone()
//                if (BuildConfig.DEBUG) "${TAG}:${Const.Toast.BIND_VIEWHOLDER_TYPE_WARN}\n${holder}".showToast()
            }
        }
    }

    /**
     * 根据屏幕比例计算图片高
     *
     * @param originalWidth   服务器图片原始尺寸：宽
     * @param originalHeight  服务器图片原始尺寸：高
     * @return 根据比例缩放后的图片高
     */
    private fun calculateImageHeight(originalWidth: Int, originalHeight: Int): Int {
        //服务器数据异常处理
        if (originalWidth == 0 || originalHeight == 0) {
            return mMaxWidth
        }
        return mMaxWidth * originalHeight / originalWidth
    }

    class SquareCardOfCommunityContentAdapter(
        val fragment: CommunityRecommendFragment,
        val dataList: List<HomePageDiscovery.ItemX>
    ) : RecyclerView.Adapter<SquareCardOfCommunityContentAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivBgPicture: ImageView = view.findViewById(R.id.ivBgPicture)
            val tvTitle: TextView = view.findViewById(R.id.tvTitle)
            val tvSubTitle: TextView = view.findViewById(R.id.tvSubTitle)
        }

        override fun getItemCount() = dataList.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SquareCardOfCommunityContentAdapter.ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_community_horizontal_scroll_card_itemcollection_item_type,
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(
            holder: SquareCardOfCommunityContentAdapter.ViewHolder,
            position: Int
        ) {
            val item = dataList[position]
            holder.ivBgPicture.layoutParams.width = mMaxWidth
            holder.ivBgPicture.load(item.data.bgPicture)
            holder.tvTitle.text = item.data.title
            holder.tvSubTitle.text = item.data.subTitle
            holder.itemView.setOnClickListener {
//                ActionUrlUtil.process(
//                    fragment,
//                    item.data.actionUrl,
//                    item.data.title
//                )
            }
        }
    }

    companion object {
        const val TAG = "CommendAdapter"
        val mMaxWidth = (screenWidth - dp2px(28f) - dp2px(6f)) / 2

        const val STR_HORIZONTAL_SCROLLCARD_TYPE = "horizontalScrollCard"
        const val STR_COMMUNITY_COLUMNS_CARD = "communityColumnsCard"

        const val STR_HORIZONTAL_SCROLLCARD_DATA_TYPE = "HorizontalScrollCard"
        const val STR_ITEM_COLLECTION_DATA_TYPE = "ItemCollection"
        const val STR_FOLLOW_CARD_DATA_TYPE = "FollowCard"

        const val STR_VIDEO_TYPE = "video"
        const val STR_UGC_PICTURE_TYPE = "ugcPicture"

        const val HORIZONTAL_SCROLLCARD_ITEM_COLLECTION_TYPE =
            1   //type:horizontalScrollCard -> dataType:ItemCollection
        const val HORIZONTAL_SCROLLCARD_TYPE =
            2                   //type:horizontalScrollCard -> dataType:HorizontalScrollCard
        const val FOLLOW_CARD_TYPE =
            3                             //type:communityColumnsCard -> dataType:FollowCard
    }

}