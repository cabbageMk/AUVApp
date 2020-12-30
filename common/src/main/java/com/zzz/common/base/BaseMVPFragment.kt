package com.zzz.common.base

import android.os.Bundle

abstract class BaseMVPFragment<V, T : BasePresenter<V>> : BaseFragment(), IBaseView {

    protected var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
        mPresenter = null
    }

    abstract fun getPresenter(): T?

    override fun showLoading() {
        showLoadingView()
    }

    override fun hideLoading() {
        hideLoadingView()
    }
}