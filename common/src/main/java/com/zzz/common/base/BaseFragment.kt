package com.zzz.common.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.zzz.common.R

abstract class BaseFragment: Fragment() {

    protected var mActivity: Activity? = null
    private val baseLoadingView by lazy {
        mActivity?.let {
            LoadingView(
                it,
                R.style.transparent_dialog
            )
        }
    }
    private val innerReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent == null || intent.action == null) {
                    return
                }
                onHandleReceiver(context, intent)
            }
        }
    }

    protected open fun onHandleReceiver(context: Context?, intent: Intent) {

    }

    protected fun registerReceiverAction(actionArray: Array<String>) {
        if (actionArray.isEmpty()) return
        val intentfilter = IntentFilter()
        if (actionArray != null) {
            for (action in actionArray) {
                intentfilter.addAction(action)
            }
        }
        mActivity?.registerReceiver(innerReceiver, intentfilter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity?.unregisterReceiver(innerReceiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (getLayoutId() != 0) {
            inflater.inflate(getLayoutId(), container, false)
        } else {
            return super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    abstract fun getLayoutId(): Int

    fun showLoadingView() {
        baseLoadingView?.show()
    }

    fun hideLoadingView() {
        if (baseLoadingView?.isShowing!!) baseLoadingView!!.cancel()
    }

    fun hideSoftKeyboard() {
        val inputMethodManager = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val localView = mActivity?.currentFocus
        if (localView != null && localView.windowToken != null) {
            val windowToken = localView.windowToken
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}