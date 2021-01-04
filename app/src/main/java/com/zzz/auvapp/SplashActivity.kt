package com.zzz.auvapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.zzz.auvapp.databinding.ActivitySplashBinding
import com.zzz.auvapp.ui.main.MainActivity
import com.zzz.common.ext.permission.isGranted
import com.zzz.common.ext.permission.request
import com.zzz.common.ext.toast
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
class SplashActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivitySplashBinding
    private var launch: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (!isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) || !isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
            ) {
                onGranted {
                    // 开启协程
                    launch = CoroutineScope(Dispatchers.Default).launch {
                        delay(3000L)
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    }
                }
                onDenied { permissions: List<String> ->
                    when (permissions[0]) {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ->
                            toast(R.string.request_permission_picture_processing)
                        Manifest.permission.READ_PHONE_STATE ->
                            toast(R.string.request_permission_access_phone_info)
                    }
                    finish()
                }

            }
        } else {
            // 开启协程
            launch = CoroutineScope(Dispatchers.Default).launch {
                delay(3000L)
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }

        // 动画
        viewBinding.ivSlogan.startAnimation(AlphaAnimation(0.5f, 1.0f).apply {
            duration = 3000L
            fillAfter = true
        })
        viewBinding.ivSplashPicture.startAnimation(ScaleAnimation(
            1f, 1.05f, 1f, 1.05f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 3000L
            fillAfter = true
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        launch?.cancel()
    }
}