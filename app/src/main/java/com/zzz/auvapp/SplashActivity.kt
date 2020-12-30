package com.zzz.auvapp

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.zzz.auvapp.databinding.ActivitySplashBinding
import com.zzz.auvapp.ui.main.MainActivity
import kotlinx.coroutines.*

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2020/12/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SplashActivity: AppCompatActivity() {
    private lateinit var viewBinding: ActivitySplashBinding
    private lateinit var launch: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // 开启协程
        launch = CoroutineScope(Dispatchers.Default).launch {
            delay(3000L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }

        // 动画
        viewBinding.ivSlogan.startAnimation(AlphaAnimation(0.5f, 1.0f).apply {
            duration = 3000L
            fillAfter = true
        })
        viewBinding.ivSplashPicture.startAnimation(ScaleAnimation(1f, 1.05f, 1f, 1.05f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = 3000L
            fillAfter = true
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        launch.cancel()
    }
}