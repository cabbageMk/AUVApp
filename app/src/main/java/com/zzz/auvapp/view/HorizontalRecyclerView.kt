package com.zzz.auvapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/02
 *     desc   : 处理嵌套ViewPager时，横向滑动冲突。
 *     version: 1.0
 * </pre>
 */
class HorizontalRecyclerView: RecyclerView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    private var lastX = 0f

    private var lastY = 0f

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        val y = ev.y
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                val deltaY = y - lastY
                if (abs(deltaX) < abs(deltaY)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {
            }
        }
        lastX = x
        lastY = y
        return super.dispatchTouchEvent(ev)
    }
}