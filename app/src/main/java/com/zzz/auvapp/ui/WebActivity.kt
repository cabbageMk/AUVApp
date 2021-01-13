package com.zzz.auvapp.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*
import com.tencent.sonic.sdk.*
import com.zzz.auvapp.R
import com.zzz.auvapp.ui.tencent.SonicJavaScriptInterface
import com.zzz.auvapp.ui.tencent.SonicSessionClientImpl
import com.zzz.auvapp.util.preCreateSession
import com.zzz.common.base.BaseActivity
import com.zzz.common.ext.logd
import com.zzz.common.ext.toast
import com.zzz.common.uitls.GlobalUtil
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.ref.WeakReference


/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class WebActivity : BaseActivity() {
    private var titleStr: String = ""

    private var linkUrl: String = ""

    private var isShare: Boolean = false

    private var isTitleFixed: Boolean = false

    private var sonicSession: SonicSession? = null

    private var sonicSessionClient: SonicSessionClientImpl? = null

    private var mode: Int = MODE_DEFAULT

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        initData()
        preloadInitVasSonic()
        super.onCreate(savedInstanceState)
    }

    /**
     * 使用VasSonic框架提升H5首屏加载速度。
     */
    private fun preloadInitVasSonic() {
        window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)

        // init sonic engine if necessary, or maybe u can do this when application created
//        if (!SonicEngine.isGetInstanceAllowed()) {
//            SonicEngine.createInstance(SonicRuntimeImpl(application), SonicConfig.Builder().build())
//        }

        // if it's sonic mode , startup sonic session at first time
        if (MODE_DEFAULT != mode) { // sonic mode
            val sessionConfigBuilder = SonicSessionConfig.Builder()
            sessionConfigBuilder.setSupportLocalServer(true)

            // if it's offline pkg mode, we need to intercept the session connection
            if (MODE_SONIC_WITH_OFFLINE_CACHE == mode) {
                sessionConfigBuilder.setCacheInterceptor(object : SonicCacheInterceptor(null) {
                    override fun getCacheData(session: SonicSession): String? {
                        return null // offline pkg does not need cache
                    }
                })
                sessionConfigBuilder.setConnectionInterceptor(object :
                    SonicSessionConnectionInterceptor() {
                    override fun getConnection(
                        session: SonicSession,
                        intent: Intent
                    ): SonicSessionConnection {
                        return OfflinePkgSessionConnection(this@WebActivity, session, intent)
                    }
                })
            }

            // create sonic session and run sonic flow
            sonicSession = SonicEngine.getInstance().createSession(
                linkUrl,
                sessionConfigBuilder.build()
            )
            if (null != sonicSession) {
                sonicSession?.bindClient(SonicSessionClientImpl().also { sonicSessionClient = it })
            } else {
                // this only happen when a same sonic session is already running,
                // u can comment following codes to feedback as a default mode.
                // throw new UnknownError("create session fail!");
                "${title},${linkUrl}:create sonic session fail!".logd(TAG)
            }
        }
    }

    override fun initData() {
        titleStr = intent.getStringExtra(TITLE) ?: GlobalUtil.appName
        linkUrl = intent.getStringExtra(LINK_URL) ?: DEFAULT_URL
        isShare = intent.getBooleanExtra(IS_SHARE, false)
        isTitleFixed = intent.getBooleanExtra(IS_TITLE_FIXED, false)
        mode = intent.getIntExtra(PARAM_MODE, MODE_DEFAULT)
    }

    override fun initView() {
        setTitle(titleStr)
        if (isShare) {
            setRightImage(R.drawable.ic_share_gray_20dp)
            setRightOnClick { toast("zzz") }
        }
        webView = findViewById<WebView>(R.id.web)
        webView.settings.run {
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            javaScriptEnabled = true
            webView.removeJavascriptInterface("searchBoxJavaBridge_")
            intent.putExtra(
                SonicJavaScriptInterface.PARAM_LOAD_URL_TIME,
                System.currentTimeMillis()
            )
            webView.addJavascriptInterface(
                SonicJavaScriptInterface(sonicSessionClient, intent),
                "sonic"
            )
            allowContentAccess = true
            databaseEnabled = true
            domStorageEnabled = true
            setAppCacheEnabled(true)
            savePassword = false
            saveFormData = false
            useWideViewPort = true
            loadWithOverviewMode = true
            defaultTextEncodingName = "UTF-8"
            setSupportZoom(true)
        }
        webView.webChromeClient = UIWebChromeClient()
        webView.webViewClient = UIWebViewClient()

        if (sonicSessionClient != null) {
            sonicSessionClient?.bindWebView(webView)
            sonicSessionClient?.clientReady()
        } else {
            webView.loadUrl(linkUrl)
        }
    }

    override fun getLayoutId() = R.layout.layout_web

    override fun onBackPressed() {
        if (webView.canGoBack()) webView.goBack() else finish()
    }

    override fun onDestroy() {
        webView.destroy()
        sonicSession?.destroy()
        sonicSession = null
        super.onDestroy()
    }

    inner class UIWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            "onPageStarted >>> url:${url}".logd(TAG)
            linkUrl = url
            super.onPageStarted(view, url, favicon)
            showLoadingView()
        }

        override fun onPageFinished(view: WebView, url: String) {
            "onPageFinished >>> url:${url}".logd(TAG)
            super.onPageFinished(view, url)
            sonicSession?.sessionClient?.pageFinish(url)
            hideLoadingView()
        }

        override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
            if (sonicSession != null) {
                val requestResponse = sonicSessionClient?.requestResource(url)
                if (requestResponse is WebResourceResponse) return requestResponse
            }
            return null
        }
    }

    inner class UIWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            "onReceivedTitle >>> title:${title}".logd(TAG)
            if (!isTitleFixed) {
                title?.run {
                    setTitle(this)
                }
            }
        }
    }

    inner class OfflinePkgSessionConnection(
        context: Context?,
        session: SonicSession?,
        intent: Intent?
    ) : SonicSessionConnection(session, intent) {

        private val context: WeakReference<Context> = WeakReference<Context>(context)

        override fun internalConnect(): Int {
            val ctx: Context = context.get()!!
            if (null != ctx) {
                try {
                    val offlineHtmlInputStream: InputStream =
                        ctx.assets.open("sonic-demo-index.html")
                    responseStream = BufferedInputStream(offlineHtmlInputStream)
                    return SonicConstants.ERROR_CODE_SUCCESS
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            return SonicConstants.ERROR_CODE_UNKNOWN
        }

        override fun internalGetResponseStream(): BufferedInputStream {
            return responseStream
        }

        override fun internalGetCustomHeadFieldEtag(): String {
            return CUSTOM_HEAD_FILED_ETAG
        }

        override fun disconnect() {
            if (null != responseStream) {
                try {
                    responseStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        override fun getResponseCode(): Int {
            return 200
        }

        override fun getResponseHeaderFields(): Map<String, List<String>> {
            return HashMap(0)
        }

        override fun getResponseHeaderField(key: String): String {
            return ""
        }

    }

    companion object {

        const val TAG = "WebActivity"

        private const val TITLE = "title"

        private const val LINK_URL = "link_url"

        private const val IS_SHARE = "is_share"

        private const val IS_TITLE_FIXED = "isTitleFixed"

        const val MODE_DEFAULT = 0

        const val MODE_SONIC = 1

        const val MODE_SONIC_WITH_OFFLINE_CACHE = 2

        const val PARAM_MODE = "param_mode"

        const val DEFAULT_URL = "https://github.com/VIPyinzhiwei/Eyepetizer"

        val DEFAULT_TITLE = GlobalUtil.appName


        /**
         * 跳转WebView网页界面
         *
         * @param context       上下文环境
         * @param title         标题
         * @param url           加载地址
         * @param isShare       是否显示分享按钮
         * @param isTitleFixed  是否固定显示标题，不会通过动态加载后的网页标题而改变。true：固定，false 反之。
         * @param mode          加载模式：MODE_DEFAULT 默认使用WebView加载；MODE_SONIC 使用VasSonic框架加载； MODE_SONIC_WITH_OFFLINE_CACHE 使用VasSonic框架离线加载
         */
        fun start(
            context: Context,
            title: String,
            url: String,
            isShare: Boolean = true,
            isTitleFixed: Boolean = false,
            mode: Int = MODE_SONIC
        ) {
            url.preCreateSession()  //预加载url
            val intent = Intent(context, WebActivity::class.java).apply {
                putExtra(TITLE, title)
                putExtra(LINK_URL, url)
                putExtra(IS_SHARE, isShare)
                putExtra(IS_TITLE_FIXED, isTitleFixed)
                putExtra(PARAM_MODE, mode)
                putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis())
            }
            context.startActivity(intent)
        }
    }
}