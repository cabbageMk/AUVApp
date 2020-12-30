package com.zzz.common.base

open class BasePresenter<V> {

    protected var mView: V? = null

    fun attachView(view: V) {
        this.mView = view
    }

    fun detachView() {
        this.mView = null
    }

    fun isAttach(): Boolean {
        return mView != null
    }

}