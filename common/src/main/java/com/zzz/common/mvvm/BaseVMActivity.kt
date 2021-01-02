package com.zzz.common.mvvm

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.zzz.common.R
import com.zzz.common.base.LoadingView
import com.zzz.common.uitls.ActivityUtil
import java.lang.ref.WeakReference

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2020/12/30
 *     desc   :
 *     version: 1.0
 * </pre>
 */
abstract class BaseVMActivity : AppCompatActivity() {
    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy {
        DataBindingUtil.setContentView<T>(this, resId).apply {
            lifecycleOwner = this@BaseVMActivity
        }
    }

    private lateinit var weakRefActivity: WeakReference<Activity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weakRefActivity = WeakReference(this)
        ActivityUtil.addActivity(weakRefActivity)

        initView()
        initData()
        openObserve()
    }

    abstract fun openObserve()

    abstract fun initView()

    abstract fun initData()

    // loading
    private val mLoadingView by lazy {
        LoadingView(
            this,
            R.style.transparent_dialog
        )
    }

    fun showLoadingView() {
        mLoadingView.show()
    }

    fun hideLoadingView() {
        if (mLoadingView.isShowing) mLoadingView.cancel()
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

    /**
     * 设置状态栏背景色
     */
    open fun setStatusBarBackground(@ColorRes statusBarColor: Int) {
        ImmersionBar.with(this).autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(statusBarColor).fitsSystemWindows(true).init()
    }
}