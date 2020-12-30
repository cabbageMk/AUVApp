package com.zzz.auvapp.ui

import android.annotation.SuppressLint
import android.view.Gravity
import com.zzz.auvapp.R
import com.zzz.auvapp.databinding.ActivityMainBinding
import com.zzz.common.mvvm.BaseVMActivity

class MainActivity : BaseVMActivity() {
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)

    override fun openObserve() {

    }

    @SuppressLint("WrongConstant")
    override fun initView() {
        setStatusBarBackground(R.color.purple_700)
//        binding.mainDrawer.openDrawer(Gravity.START)
        binding.run {  }
    }

    override fun initData() {

    }
}