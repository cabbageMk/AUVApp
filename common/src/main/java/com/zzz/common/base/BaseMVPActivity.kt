package com.zzz.common.base

import android.os.Bundle

abstract class BaseMVPActivity<V, T : BasePresenter<V>> : BaseActivity(), IBaseView {

    private var mPresenter: T? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = getPresenter()
        mPresenter?.attachView(this as V)
    }

    override fun showLoading() {
        showLoadingView()
    }

    override fun hideLoading() {
        hideLoadingView()
    }

    abstract fun getPresenter(): T?
}