package com.zzz.common.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zzz.common.R
import com.zzz.common.uitls.ActivityUtil
import java.lang.ref.WeakReference


abstract class BaseActivity : AppCompatActivity() {
    private var weakRefActivity: WeakReference<Activity>? = null
    private val baseLoadingView by lazy {
        LoadingView(
            this,
            R.style.transparent_dialog
        )
    }
    private lateinit var viewContent: FrameLayout
    private lateinit var imgBack: ImageView
    private lateinit var imgRight: ImageView
    private lateinit var flTitle: FrameLayout
    private lateinit var txtTitle: TextView
    private lateinit var txtRight: TextView

    private val innerReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent == null || intent.action == null) {
                    return
                }
                onHandleReceiver(context, intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        weakRefActivity = WeakReference(this)
        ActivityUtil.addActivity(weakRefActivity)
        setContentView(R.layout.activity_base)
        viewContent = findViewById(R.id.view_content)
        imgBack = findViewById(R.id.imgBack)
        flTitle = findViewById(R.id.fl_title)
        txtTitle = findViewById(R.id.txtTitle)
        txtRight = findViewById(R.id.txtRight)
        imgRight = findViewById(R.id.imgRight)
        LayoutInflater.from(this).inflate(getLayoutId(), viewContent)
        imgBack.setOnClickListener {
            onBackClick()
        }

        val actionArray = getReceiverAction()
        val intentfilter = IntentFilter()
        if (actionArray != null) {
            for (action in actionArray) {
                intentfilter.addAction(action)
            }
        }
        registerReceiver(innerReceiver, intentfilter)

        initView()
        initData()
    }

    /**
     * 返回按钮点击事件
     */
    protected open fun onBackClick() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(innerReceiver)
        ActivityUtil.removeActivity(weakRefActivity)
    }

    /**
     * 需要注册的广播
     *
     * @return 广播数组
     */
    protected open fun getReceiverAction(): Array<String>? {
        return null
    }

    protected open fun onHandleReceiver(context: Context?, intent: Intent) {

    }

    fun showLoadingView() {
        baseLoadingView.show()
    }

    fun hideLoadingView() {
        if (baseLoadingView.isShowing) baseLoadingView.cancel()
    }

    fun hideSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val localView = currentFocus
        if (localView != null && localView.windowToken != null) {
            val windowToken = localView.windowToken
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    fun hideTitleBar() {
        flTitle.visibility = View.GONE
    }

    /**
     * 设置标题栏标题
     *
     * @param title 标题
     */
    fun setTitle(title: String?) {
        txtTitle.text = title
    }

    /**
     * 设置返回按钮图片
     *
     * @param imageId 图片id
     */
    fun setBackImage(imageId: Int) {
        if (imageId != 0) {
            imgBack.setImageResource(imageId)
        }
    }

    /**
     * 设置返回按钮图片是否显示
     *
     * @param imageVisible 是否显示
     */
    fun setBackImageVisible(imageVisible: Boolean) {
        imgBack.visibility = if (imageVisible) View.VISIBLE else View.GONE
    }

    /**
     * 设置右边图片
     *
     * @param imageId 图片id
     */
    fun setRightImage(imageId: Int) {
        if (imageId != 0) {
            require(txtRight.visibility != View.VISIBLE) { "文字和图片不可同时设置" }
            imgRight.visibility = View.VISIBLE
            imgRight.setImageResource(imageId)
        }
    }

    fun setRightOnClick(l: View.OnClickListener) {
        if (imgRight.visibility == View.VISIBLE) {
            imgRight.setOnClickListener ( l )
        } else if (txtRight.visibility == View.VISIBLE) {
            txtRight.setOnClickListener ( l )
        }
    }

    /**
     * 设置右边文字
     *
     * @param text 文字
     */
    fun setRightText(text: String?) {
        if (text != null) {
            require(imgRight.visibility != View.VISIBLE) { "文字和图片不可同时设置" }
            txtRight.visibility = View.VISIBLE
            txtRight.text = text
        }
    }

    /**
     * 设置右边文字背景
     *
     * @param color 颜色
     */
    fun setRightBackColor(color: Int) {
        txtRight.setBackgroundColor(color)
    }

    /**
     * 设置右边文字禁止点击
     *
     * @param click 是否可以点击
     */
    fun setRightTextClick(click: Boolean) {
        txtRight.isClickable = click
    }

    abstract fun initData()

    abstract fun initView()

    abstract fun getLayoutId(): Int

    /**
     * 将状态栏设置成透明。只适配Android 5.0以上系统的手机。
     */
    private fun transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Translucent status bar
            // Translucent status bar
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
            // Translucent navigation bar
            // Translucent navigation bar
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
        }
    }
}