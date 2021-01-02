package com.zzz.common.mvvm

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.zzz.common.R
import com.zzz.common.base.LoadingView

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2020/12/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
abstract class BaseVMFragment<T : ViewDataBinding>(@LayoutRes val contentLayoutId: Int) :
    Fragment(contentLayoutId) {
    lateinit var binding: T

    protected fun <T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        @LayoutRes layoutId: Int,
        container: ViewGroup?
    ): T = DataBindingUtil.inflate<T>(inflater, layoutId, container, false).apply {
        lifecycleOwner = this@BaseVMFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = binding(inflater, contentLayoutId, container)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initData()
        openObserve()
    }

    abstract fun openObserve()

    abstract fun initView()

    abstract fun initData()

    // loading
    private val mLoadingView by lazy {
        context?.let {
            LoadingView(
                it,
                R.style.transparent_dialog
            )
        }
    }

    fun showLoadingView() {
        mLoadingView?.show()
    }

    fun hideLoadingView() {
        if (mLoadingView?.isShowing == true) mLoadingView?.cancel()
    }

    fun hideSoftKeyboard() {
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val localView = activity?.currentFocus
        if (localView != null && localView.windowToken != null) {
            val windowToken = localView.windowToken
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}