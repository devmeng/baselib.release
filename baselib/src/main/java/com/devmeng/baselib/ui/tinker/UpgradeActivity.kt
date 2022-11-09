package com.devmeng.baselib.ui.tinker

import android.view.View
import com.devmeng.baselib.R
import com.devmeng.baselib.base.BasePresenter
import com.devmeng.baselib.base.bind.BaseBindActivity
import com.devmeng.baselib.databinding.ActivityUpgradeBinding
import com.devmeng.baselib.utils.DateUtils
import com.devmeng.baselib.utils.DigitalConvertUtils.Companion.instance
import com.devmeng.baselib.utils.Logger
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.download.DownloadListener
import com.tencent.bugly.beta.download.DownloadTask

class UpgradeActivity : BaseBindActivity<BasePresenter>() {

    private var viewBinding: ActivityUpgradeBinding? = null

    override fun createPresenter(): BasePresenter {
        return BasePresenter(this)
    }

    override fun initContentViewBinding(): View {
        viewBinding = ActivityUpgradeBinding.inflate(layoutInflater)
        return viewBinding!!.root
    }

    override fun setConfigure() {
        with(viewBinding!!) {
            tvUpgradeNextTime.setOnClickListener(this@UpgradeActivity)
            tvUpgradeStart.setOnClickListener(this@UpgradeActivity)
            val info = Beta.getAppUpgradeInfo()
            tvUpgradeTitle.text = info.title
            tvUpgradeVersionNum.text = getString(R.string.upgrade_version_num, info.versionName)
            val convertUtils = instance
            Logger.d("UpgradeActivity: 更新包大小: ${info.fileSize}")
            tvUpgradeApkSize.text = getString(
                R.string.upgrade_apk_size,
                String.format(
                    getString(R.string.keep_2_decimals),
                    convertUtils!!.byte2mb(info.fileSize.toFloat())
                )
            )

//        DateUtils dateUtils = DateUtils.getInstance();
            val dateUtils = DateUtils()
            val upgradeTime =
                dateUtils.formatDate(DateUtils.DATE_YYYY_MM_DD_TIME_MINUS_PATTERN, info.publishTime)
            tvUpgradeTime.text = getString(R.string.upgrade_time, upgradeTime)
            tvUpgradeIntroduceContent.text = info.newFeature

        }
        val downloadTask = Beta.getStrategyTask()
        updateBtnState(downloadTask)

        //注册下载监听，监听下载事件
        Beta.registerDownloadListener(object : DownloadListener {
            override fun onReceive(task: DownloadTask) {
                updateBtnState(task)
            }

            override fun onCompleted(task: DownloadTask) {
                updateBtnState(task)
                Beta.installApk(task.saveFile)
            }

            override fun onFailed(task: DownloadTask, code: Int, extMsg: String) {
                updateBtnState(task)
            }
        })
    }

    override fun initData() {}

    override fun onClick(v: View?) {
        with(viewBinding!!) {
            when (v) {
                tvUpgradeNextTime -> {
                    //下次再说
                    Beta.cancelDownload()
                    finish()
                }
                tvUpgradeStart -> {
                    //立即体验
                    val status = Beta.getStrategyTask().status
                    if (status == DownloadTask.DOWNLOADING) {
                        return
                    }
                    val downloadTask = Beta.startDownload()
                    updateBtnState(downloadTask)
                }
            }
        }
    }

    private fun updateBtnState(task: DownloadTask) {
        with(viewBinding!!) {
            when (task.status) {
                DownloadTask.INIT, DownloadTask.DELETED -> tvUpgradeStart.text = getString(
                    R.string.upgrade_experience
                )
                DownloadTask.FAILED -> tvUpgradeStart.text = getString(R.string.upgrade_re_download)
                DownloadTask.COMPLETE -> tvUpgradeStart.text = getString(R.string.upgrade_install)
                DownloadTask.DOWNLOADING -> {
                    val savedLength = task.savedLength
                    val totalLength = task.totalLength
                    if (totalLength == 0L) {
                        return
                    }
                    Logger.d("相除 -> ${savedLength / totalLength}")
                    val dbProgress = savedLength.toDouble() / totalLength
                    val progress =
                        (getString(R.string.keep_2_decimals, dbProgress).toFloat() * 100).toInt()
                    Logger.d("处理前下载进度 -> $dbProgress")
                    Logger.d("处理后下载进度 -> $progress")
                    tvUpgradeStart.text = getString(R.string.format_num_percent, progress)
                }
                DownloadTask.PAUSED -> tvUpgradeStart.setText(R.string.resume_upgrade)
            }
        }
    }

    override fun release() {
        Beta.unregisterDownloadListener()
    }
}