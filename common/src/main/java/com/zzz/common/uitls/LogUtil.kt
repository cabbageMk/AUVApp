package com.zzz.common.uitls

import android.util.Log
import com.zzz.common.BuildConfig

object LogUtil {

    private val IS_DEBUG = BuildConfig.DEBUG
    private val TAG = "Zzz"

    fun i(message: String) {
        if (IS_DEBUG) i(TAG, message)
    }

    fun i(tag: String, message: String) {
        if (IS_DEBUG) Log.i("$tag-->:", message)
    }

    fun e(message: String) {
        if (IS_DEBUG) e(TAG, message)
    }

    fun e(tag: String, message: String) {
        if (IS_DEBUG) Log.e("$tag-->", message)
    }

    fun w(message: String) {
        if (IS_DEBUG) w(TAG, message)
    }

    fun w(tag: String, message: String) {
        if (IS_DEBUG) Log.w("$tag-->:", message)
    }

    fun v(message: String) {
        if (IS_DEBUG) v(TAG, message)
    }

    fun v(tag: String, message: String) {
        if (IS_DEBUG) Log.v("$tag-->:", message)
    }

    fun d(message: String) {
        if (IS_DEBUG) d(TAG, message)
    }

    fun d(tag: String, message: String) {
        if (IS_DEBUG) Log.d("$tag-->:", message)
    }

}