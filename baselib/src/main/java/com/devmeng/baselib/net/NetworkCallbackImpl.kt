package com.devmeng.baselib.net

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Handler
import com.devmeng.baselib.utils.NetType
import com.devmeng.baselib.utils.NetworkUtils

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
class NetworkCallbackImpl : ConnectivityManager.NetworkCallback() {

    private val networkUtils: NetworkUtils = NetworkUtils.instance

    private val handler: Handler = Handler()

    override fun onAvailable(network: Network) {
        super.onAvailable(network)

    }

    override fun onLost(network: Network) {
        super.onLost(network)
        handler.post {
            networkUtils.postNetworkType(NetType.NONE)
        }
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities)
        handler.post {
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    networkUtils.postNetworkType(NetType.WIFI)
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    networkUtils.postNetworkType(NetType.CELLULAR)
                }
            } else {
                networkUtils.postNetworkType(NetType.INVALIDATED)
            }
        }
    }

}