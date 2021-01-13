package com.zzz.auvapp.ui.tencent

import android.content.Context
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import com.tencent.sonic.sdk.SonicRuntime
import com.tencent.sonic.sdk.SonicSessionClient
import com.zzz.auvapp.BuildConfig
import java.io.File
import java.io.InputStream


/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/12
 *     desc   : the sonic host application must implement SonicRuntime to do right things.
 *     version: 1.0
 * </pre>
 */
class SonicRuntimeImpl(context: Context) : SonicRuntime(context) {
    /**
     * 获取用户UA信息
     * @return
     */
    override fun getUserAgent(): String? {
//        return "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Mobile Safari/537.36"
        return WebSettings.getDefaultUserAgent(context)
    }

    /**
     * 获取用户ID信息
     * @return
     */
    override fun getCurrentUserAccount(): String? {
        return "sonic-demo-master"
    }

    override fun getCookie(url: String?): String? {
        val cookieManager: CookieManager = CookieManager.getInstance()
        return cookieManager.getCookie(url)
    }

    override fun log(tag: String?, level: Int, message: String?) {
        when (level) {
            Log.ERROR -> message?.let { Log.e(tag, it) }
            Log.INFO -> message?.let { Log.i(tag, it) }
            else -> message?.let { Log.d(tag, it) }
        }
    }

    override fun createWebResourceResponse(
        mimeType: String?,
        encoding: String?,
        data: InputStream?,
        headers: Map<String?, String?>?
    ): Any? {
        val resourceResponse = WebResourceResponse(mimeType, encoding, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            resourceResponse.responseHeaders = headers
        }
        return resourceResponse
    }

    override fun showToast(text: CharSequence?, duration: Int) {}

    override fun notifyError(client: SonicSessionClient?, url: String?, errorCode: Int) {}

    override fun isSonicUrl(url: String?): Boolean {
        return true
    }

    override fun setCookie(url: String?, cookies: List<String?>?): Boolean {
        if (!TextUtils.isEmpty(url) && cookies != null && cookies.isNotEmpty()) {
            val cookieManager: CookieManager = CookieManager.getInstance()
            for (cookie in cookies) {
                cookieManager.setCookie(url, cookie)
            }
            return true
        }
        return false
    }

    override fun isNetworkValid(): Boolean {
        return true
    }

    override fun postTaskToThread(task: Runnable?, delayMillis: Long) {
        val thread = Thread(task, "SonicThread")
        thread.start()
    }

    override fun getSonicCacheDir(): File? {
        if (BuildConfig.DEBUG) {
            val path: String = Environment.getExternalStorageDirectory()
                .absolutePath + File.separator.toString() + "sonic/"
            val file = File(path.trim { it <= ' ' })
            if (!file.exists()) {
                file.mkdir()
            }
            return file
        }
        return super.getSonicCacheDir()
    }

    override fun getHostDirectAddress(url: String?): String? {
        return null
    }
}