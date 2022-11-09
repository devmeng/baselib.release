package com.devmeng.baselib

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.devmeng.baselib.listener.NetChangeObserver
import com.devmeng.baselib.utils.Action
import com.devmeng.baselib.utils.Logger
import com.devmeng.baselib.utils.NetType
import com.devmeng.baselib.utils.NetworkUtils

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  21:55
 * Version : 1
 * 监听网络状态: WIFI / CELLULAR
 */
class NetStateReceiver : BroadcastReceiver() {

    private var netType: NetType = NetType.NONE
    var netChangeObserver: NetChangeObserver? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || intent.action == null) {
            Logger.e("网络监听广播注册异常")
            return
        }
        if (intent.action!!.equals(Action.ANDROID_NET_CHANGE_ACTION, true)) {
            Logger.d("网络发生变更")
            //赋值网络类型
            netType = NetworkUtils.getType()
            if (NetworkUtils.isNetworkAvailable()) {
                Logger.d("网络连接成功 -> $netType")
                if (netChangeObserver != null) {
                    netChangeObserver!!.onConnected(netType)
                }
            } else {
                Logger.e("网络连接异常")
                if (netChangeObserver != null) {
                    netChangeObserver!!.onDisConnect()
                }
            }
        }
    }

}