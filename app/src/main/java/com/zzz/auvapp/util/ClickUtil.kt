package com.zzz.auvapp.util

import android.view.View

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/03
 *     desc   :
 *     version: 1.0
 * </pre>
 */

/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param block 处理点击事件回调代码块
 */
fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener { it.block() }
    v.forEach { it?.setOnClickListener(listener) }
}