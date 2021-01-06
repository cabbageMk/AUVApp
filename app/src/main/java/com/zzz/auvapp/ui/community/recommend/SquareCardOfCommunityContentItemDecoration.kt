package com.zzz.auvapp.ui.community.recommend

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zzz.common.ext.dp2px

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/06
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SquareCardOfCommunityContentItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val count = parent.adapter?.itemCount?.minus(1) //item count

        when (position) {
            0 -> {
                outRect.right = dp2px(3f)
            }
            count -> {
                outRect.left = dp2px(3f)
            }
            else -> {
                outRect.left = dp2px(3f)
                outRect.right = dp2px(3f)
            }
        }
    }
}