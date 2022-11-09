package com.devmeng.baselib.utils

import android.util.Log
import com.devmeng.baselib.net.ApiConstant

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  16:14
 * Version : 1
 */
object Logger {
    var TAG: String = "Logger"

    @JvmStatic
    fun d(msg: String) {
        if (ApiConstant.BUILD_TYPE) {
            Log.d(TAG, "msg -> $msg")
        }
    }

    @JvmStatic
    fun e(errorMsg: String) {
        if (ApiConstant.BUILD_TYPE) {
            Log.e(TAG, "errorMsg -> $errorMsg")
        }
    }

    @JvmStatic
    fun i(info: String) {
        if (ApiConstant.BUILD_TYPE) {
            Log.i(TAG, "<[$info]>")
        }
    }

    @JvmStatic
    fun w(warnMsg: String) {
        if (ApiConstant.BUILD_TYPE) {
            Log.w(TAG, "<!![$warnMsg]!!>")
        }
    }
}