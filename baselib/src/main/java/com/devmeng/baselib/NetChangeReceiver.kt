package com.devmeng.baselib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.devmeng.baselib.utils.Action
import com.devmeng.baselib.utils.Logger
import com.devmeng.baselib.utils.NetType
import com.devmeng.baselib.utils.NetworkUtils

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
class NetChangeReceiver : BroadcastReceiver() {

    private var netType: NetType = NetType.AUTO

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || intent.action == null) {
            Logger.e("网络监听广播注册异常")
            return
        }
        if (intent.action!!.equals(Action.ANDROID_NET_CHANGE_ACTION, true)) {
            Logger.d("网络发生变更")
            //赋值网络类型
            netType = NetworkUtils.getType()
            /*if (NetworkUtils.isNetworkAvailable()) {
                Logger.d("网络连接成功 -> $netType")
            } else {
                Logger.e("网络连接异常")
            }*/
            NetworkUtils().postNetworkType(netType)
        }
    }

}