package com.zzz.common.net

import retrofit2.Call

class RetrofitCoroutineDsl<T> {
    var api: (Call<T>)? = null

    internal var onSuccess: ((T?) -> Unit)? = null
        private set
    internal var onComplete: (() -> Unit)? = null
        private set
    internal var onFailed: ((error: String?, errorCode: Int) -> Unit)? = null
        private set


    var showFailedMsg = false

    internal fun clean() {
        onSuccess = null
        onComplete = null
        onFailed = null
    }

    fun onSuccess(block: (T?) -> Unit) {
        this.onSuccess = block
    }

    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

    fun onFailed(block: (error: String?, errorCode: Int) -> Unit) {
        this.onFailed = block
    }
}