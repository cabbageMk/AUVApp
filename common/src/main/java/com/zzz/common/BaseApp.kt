package com.zzz.common

import android.app.Application
import android.content.Context

open class BaseApp : Application() {

    companion object {
        @JvmStatic
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}