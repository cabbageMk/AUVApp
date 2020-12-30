package com.zzz.common.uitls

import android.annotation.SuppressLint
import android.widget.Toast
import com.zzz.common.BaseApp

object ToastUtil {

    private var mToast: Toast? = null

    @SuppressLint("ShowToast")
    fun show(msg: String) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApp.context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast!!.setText(msg)
        }
        mToast?.show()
    }

    @SuppressLint("ShowToast")
    fun show(msg: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApp.context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast!!.setText(msg)
        }
        mToast?.show()
    }
}