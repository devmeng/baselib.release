package com.devmeng.baselib.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import com.devmeng.baselib.NetChangeReceiver
import com.devmeng.baselib.NetStateReceiver
import com.devmeng.baselib.listener.NetChangeObserver
import com.devmeng.baselib.net.NetworkCallbackImpl

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
class NetworkManager private constructor() {

    var application: Application? = null

    private var netStateReceiver =
        NetStateReceiver()

    private var netChangeReceiver =
        NetChangeReceiver()

    companion object {

        val instance: NetworkManager by lazy(
            mode = LazyThreadSafetyMode.SYNCHRONIZED,
        ) {
            NetworkManager()
        }

    }

    fun registerNetworkListener(objects: Any) {
        NetworkUtils.instance.register(objects)
    }

    fun registerNetworkStateReceiver(application: Application) {
        this.application = application
        //动态注册广播
        val filter = IntentFilter()
        filter.addAction(Action.ANDROID_NET_CHANGE_ACTION)
        application.registerReceiver(netStateReceiver, filter)
    }

    @SuppressLint("ObsoleteSdkInt")
    fun registerNetworkListener(application: Application) {
        this.application = application
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val networkRequest = NetworkRequest.Builder().build()
            val connMgr =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connMgr.registerNetworkCallback(networkRequest, NetworkCallbackImpl())
        } else {
            val filter = IntentFilter()
            filter.addAction(Action.ANDROID_NET_CHANGE_ACTION)
            application.registerReceiver(netChangeReceiver, filter)
        }
    }

    fun unregisterNetworkStateReceiver(application: Application) {
        application.unregisterReceiver(netStateReceiver)
    }

    fun unregisterNetChangeReceiver(application: Application) {
        application.unregisterReceiver(netChangeReceiver)
    }

    fun setNetworkChangeObserver(observer: NetChangeObserver) {
//        networkStateBroadcastReceiver.netChangeObserver = observer
    }


}