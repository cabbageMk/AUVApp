package com.zzz.auvapp.ui.tencent

import android.os.Bundle
import android.webkit.WebView
import com.tencent.sonic.sdk.SonicSessionClient

/**
 * <pre>
 *     author : zheng
 *     e-mail :
 *     time   : 2021/01/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class SonicSessionClientImpl : SonicSessionClient() {

    private var webView: WebView? = null

    fun bindWebView(webView: WebView?) {
        this.webView = webView
    }

    fun getWebView(): WebView? {
        return webView
    }

    override fun loadUrl(url: String?, extraData: Bundle?) {
        if (url != null) {
            webView?.loadUrl(url)
        }
    }

    override fun loadDataWithBaseUrl(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?
    ) {
        if (data != null) {
            webView?.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
        }
    }


    override fun loadDataWithBaseUrlAndHeader(
        baseUrl: String?,
        data: String?,
        mimeType: String?,
        encoding: String?,
        historyUrl: String?,
        headers: HashMap<String?, String?>?
    ) {
        loadDataWithBaseUrl(baseUrl, data, mimeType, encoding, historyUrl)
    }

    fun destroy() {
        if (null != webView) {
            webView!!.destroy()
            webView = null
        }
    }
}