package com.zzz.auvapp.ui.home.discovery

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import com.zzz.auvapp.logic.model.HomePageDiscovery
import com.zzz.auvapp.util.load


/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/05
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MyBannerAdapter(val dataList: ArrayList<HomePageDiscovery.ItemX>) :
    BannerAdapter<HomePageDiscovery.ItemX, MyBannerAdapter.BannerViewHolder>(dataList) {

    inner class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView

        init {
            imageView = view
        }
    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent!!.context)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: HomePageDiscovery.ItemX?,
        position: Int,
        size: Int
    ) {
        data?.data?.image?.let { holder?.imageView?.load(it) };
    }
}