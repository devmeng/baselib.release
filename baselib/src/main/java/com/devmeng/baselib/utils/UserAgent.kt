package com.devmeng.baselib.utils

import android.content.Context
import android.webkit.WebSettings

/**
 * Created by Richard -> MHS
 * Date : 2022/5/31  20:43
 * Version : 1
 */
open class UserAgent {

    companion object {
        fun instance(): UserAgent {
            return UserAgent()
        }
    }

    open fun getUserAgentInfo(context: Context, extra: String): String {
        val defaultUserAgent = WebSettings.getDefaultUserAgent(context)
        val userAgent = StringBuilder()
        userAgent.append(defaultUserAgent).append(MD5Utils().getMD5Code(extra).toString())
        Logger.e("extra $extra : userAgent md5 -> $userAgent")
        return userAgent.toString()
    }


}