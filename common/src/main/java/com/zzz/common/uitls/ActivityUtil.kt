package com.zzz.common.uitls

import android.app.Activity
import java.lang.ref.WeakReference

object ActivityUtil {

    private val activityList = mutableListOf<WeakReference<Activity>?>()

    fun addActivity(activity: WeakReference<Activity>?) {
        activityList.add(activity)
    }

    fun removeActivity(activity: WeakReference<Activity>?) {
        activityList.remove(activity)
    }

    fun finishAll() {
        if (activityList.isNotEmpty()) {
            for (activityWeakReference in activityList) {
                val activity = activityWeakReference?.get()
                if (activity != null && !activity.isFinishing) {
                    activity.finish()
                }
            }
            activityList.clear()
        }
    }
}