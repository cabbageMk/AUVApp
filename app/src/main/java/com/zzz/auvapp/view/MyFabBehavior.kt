package com.zzz.auvapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.marginBottom

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/02
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class MyFabBehavior(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {
    //动画是否在进行
    private var hide = false

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        // 判断是垂直滚动
        return axes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {
        val animate = child.animate()
        animate.duration = 500
        //计算出需要移动的高度
        val h = child.measuredHeight + child.marginBottom
        if (hide) {
            if (dy < 0) {
                // 向下滑
                animate.translationY(0f).start()
                hide = false
            }
        } else {
            if (dy > 0) {
                // 向上滑
                animate.translationY(h.toFloat()).start()
                hide = true
            }
        }
    }
}