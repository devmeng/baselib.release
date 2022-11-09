package com.devmeng.baselib.base

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.devmeng.baselib.R
import com.devmeng.baselib.base.bind.BaseBindActivity
import com.devmeng.baselib.databinding.ActivityBaseWebBinding
import com.devmeng.baselib.utils.EMPTY
import com.devmeng.baselib.utils.Logger
import com.devmeng.baselib.utils.UserAgent

/**
 * Created by Richard -> MHS
 * Date : 2022/5/31  19:09
 * Version : 1
 */
abstract class BaseWebActivity : BaseBindActivity<BasePresenter>() {

    companion object {
        //可使用 BuildConfig 中的 DEBUG 做统一处理
        const val isH5Debug = true

        fun launcher(activity: AppCompatActivity, bundle: Bundle) {
            val intent = Intent(activity, BaseWebActivity::class.java)
            intent.putExtras(bundle)
            activity.startActivity(intent)
        }
    }

    abstract fun webUrl(): String

    abstract fun webTitle(): String

    abstract fun isNeedToolBar(): Boolean

    private lateinit var baseWebBinding: ActivityBaseWebBinding

    override fun initContentViewBinding(): View {
        baseWebBinding = ActivityBaseWebBinding.inflate(layoutInflater)
        return baseWebBinding.root
    }

    override fun createPresenter(): BasePresenter {
        return BasePresenter(this)
    }

    private val webSettings: WebSettings
        get() {
            return baseWebBinding.webView.settings
        }

    override fun setConfigure() {
        baseWebBinding.layoutBaseToolbar.tvBaseTbTitle.text = webTitle()
        baseWebBinding.layoutBaseToolbar.imgBaseTbClose.setOnClickListener(this)

        baseWebBinding.layoutBaseToolbar.tvBaseTbTitle.ellipsize = TextUtils.TruncateAt.MARQUEE

        //todo webView debug 模式
        //当应用的 debug 调试模式为 true 时，WebView 进入 debug 调试模式
        WebView.setWebContentsDebuggingEnabled(isH5Debug)

        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        webSettings.userAgentString = UserAgent.instance().getUserAgentInfo(this, "test")
        baseWebBinding.webView.webChromeClient = object : WebChromeClient() {

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                Logger.e("web title -> $title")
            }
        }

//        web_view.addJavascriptInterface()

        baseWebBinding.webView.loadUrl(webUrl())

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        if (v == baseWebBinding.layoutBaseToolbar.imgBaseTbClose) {
            finish()
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (baseWebBinding.webView.canGoBack() and (keyCode == KeyEvent.KEYCODE_BACK)) {
            baseWebBinding.webView.goBack()
        }
        return super.onKeyUp(keyCode, event)
    }

    override fun release() {
        baseWebBinding.webView.destroy()
        webSettings.javaScriptEnabled = false
    }


    protected open fun evaluate(
        h5Method: String?,
        callback: ValueCallback<String?>?,
        vararg params: String?
    ) {
        runOnUiThread(EvaluateH5Method(this, h5Method, params, callback))
    }

    internal class EvaluateH5Method(
        var activity: BaseWebActivity,
        var h5Method: String?,
        var params: Array<out String?>, /* synthetic */
        var callback: ValueCallback<String?>?
    ) :
        RebuildParamRunnable(params) {

        override fun getParams(params: String?) {
            val script = activity.getString(
                R.string.format_android_call_h5_method,
                h5Method, params
            )
            Logger.d("script -> $script")
            activity.baseWebBinding.webView.evaluateJavascript(
                script, callback
            )
        }
    }

    internal abstract class RebuildParamRunnable(
        private val mParams: Array<out String?>
    ) :
        BaseRunnable {
        protected abstract fun getParams(str: String?)
        override fun run() {
            val paramsBuffer = StringBuffer(EMPTY)

            for (index in mParams.indices) {
                val param = mParams[index]
                if (param != null) {
                    paramsBuffer.append("'$param',")
                }
                if ((index == mParams.size - 1) and (paramsBuffer[paramsBuffer.length - 1].toString() == ",")) {
                    paramsBuffer.deleteCharAt(paramsBuffer.length - 1)
                }
            }
            getParams(paramsBuffer.toString())

        }
    }

}