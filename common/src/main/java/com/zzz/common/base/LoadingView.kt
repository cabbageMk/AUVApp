package com.zzz.common.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.wang.avi.AVLoadingIndicatorView
import com.zzz.common.R

class LoadingView :AlertDialog {

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_loading_view)
        findViewById<AVLoadingIndicatorView>(R.id.loadingView)?.smoothToShow()

        setCanceledOnTouchOutside(false)
    }
}