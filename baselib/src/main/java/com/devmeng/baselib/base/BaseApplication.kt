package com.devmeng.baselib.base

import android.app.Application
import android.content.Intent
import com.devmeng.baselib.R
import com.devmeng.baselib.skin.SkinManager
import com.devmeng.baselib.ui.tinker.UpgradeActivity
import com.devmeng.baselib.utils.Logger
import com.devmeng.baselib.utils.NetworkManager
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.UpgradeInfo
import com.tencent.bugly.beta.download.DownloadListener
import com.tencent.bugly.beta.download.DownloadTask
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.beta.upgrade.UpgradeStateListener

/**
 * Created by Richard -> MHS
 * Date : 2022/5/31  14:59
 * Version : 1
 */
open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //动态换肤功能初始化
        SkinManager.init(this)

        NetworkManager.instance.registerNetworkListener(this)

    }

    override fun onTerminate() {
        super.onTerminate()
        NetworkManager.instance.unregisterNetChangeReceiver(this)
    }

    /**
     * 初始化 热更新 SDK
     */
    protected fun initBugly() {
        val id = applicationContext.resources.getString(R.string.tinker_app_id)

        Beta.autoInit = true
        Beta.autoInstallApk = true
        Beta.autoCheckUpgrade = true
        Beta.upgradeCheckPeriod = (5 * 1000).toLong()

//        Beta.largeIconId = R.mipmap.ic_launcher;//设置通知栏图标

        //监听安装包下载状态
        Beta.downloadListener = object : DownloadListener {
            override fun onReceive(downloadTask: DownloadTask?) {
                downloadTask!!.isNeededNotify = true;
                val progress = downloadTask.savedLength / downloadTask.totalLength * 100;
                Logger.d("downloadListener receive apk file => $progress%");
            }

            override fun onCompleted(downloadTask: DownloadTask?) {
                Logger.d("downloadListener download apk file success");
            }

            override fun onFailed(downloadTask: DownloadTask?, p1: Int, p2: String?) {
                Logger.d("downloadListener download apk file fail");
            }

        }

        //检测是否有更新监听
        Beta.upgradeListener =
            UpgradeListener { ret: Int, strategy: UpgradeInfo?, isManual: Boolean, isSilence: Boolean ->
                if (strategy != null) {
                    val intent = Intent()
                    intent.setClass(applicationContext, UpgradeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    applicationContext.startActivity(intent)
                }
            }

        //监听APP升级状态
        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onUpgradeFailed(b: Boolean) {
                Logger.d(
                    "检查更新失败"
                )
            }

            override fun onUpgradeSuccess(b: Boolean) {
                Logger.d(
                    "检查更新完成"
                )
            }

            override fun onUpgradeNoVersion(b: Boolean) {
                Logger.d(
                    "检查没有更新"
                )
            }

            override fun onUpgrading(b: Boolean) {
                Logger.d(
                    "正在检查更新"
                )
            }

            override fun onDownloadCompleted(b: Boolean) {
                Logger.d(
                    "更新包下载完成"
                )
            }
        }
        //Debug:true  Release:false
        Bugly.init(applicationContext, id, true)
    }

}