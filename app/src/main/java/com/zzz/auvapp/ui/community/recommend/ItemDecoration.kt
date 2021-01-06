package com.zzz.auvapp.ui.community.recommend

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zzz.auvapp.R
import com.zzz.common.BaseApp
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
class ItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex

        outRect.top = BaseApp.context.resources.getDimensionPixelOffset(R.dimen.dp_14)

        when (spanIndex) {
            0 -> {
                outRect.left = BaseApp.context.resources.getDimensionPixelOffset(R.dimen.dp_14)
                outRect.right = dp2px(3f)
            }
            else -> {
                outRect.left = dp2px(3f)
                outRect.right = BaseApp.context.resources.getDimensionPixelOffset(R.dimen.dp_14)
            }
        }
    }
}