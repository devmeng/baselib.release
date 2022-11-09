package com.devmeng.baselib.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.devmeng.baselib.annotation.Network
import com.devmeng.baselib.entities.MethodManager

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
class NetworkUtils {

    private var networkList: HashMap<Any, MutableList<MethodManager>> = HashMap()

    companion object {

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkUtils()
        }

        fun isNetworkAvailable(): Boolean {
            val connMgr: ConnectivityManager =
                NetworkManager.instance.application!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                    ?: return false

            val activeNetwork = connMgr.activeNetwork ?: return false

            return connMgr.getNetworkCapabilities(activeNetwork)!!
                .hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    connMgr.getNetworkCapabilities(activeNetwork)!!
                        .hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }

        fun getType(): NetType {

            val connMgr: ConnectivityManager =
                NetworkManager.instance.application!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
                    ?: return NetType.NONE

            val activeNetwork = connMgr.activeNetwork ?: return NetType.NONE

            if (connMgr.getNetworkCapabilities(activeNetwork)!!
                    .hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            ) {
                return NetType.WIFI
            } else if (connMgr.getNetworkCapabilities(activeNetwork)!!
                    .hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            ) {
                return NetType.CELLULAR
            }

            return NetType.NONE
        }

    }

    fun postNetworkType(netType: NetType) {
        if (networkList.isEmpty()) return
        val keys = networkList.keys
        for (key in keys) {
            val methodList = networkList[key]
            if (methodList.isNullOrEmpty()) break
            for (methodManager in methodList) {
                if (methodManager.type.isAssignableFrom(NetType::class.java)) {
                    when (methodManager.netType) {
                        NetType.AUTO -> {
                            invoke(methodManager, key, netType)
                        }
                        NetType.WIFI -> {
                            if (netType == NetType.WIFI || netType == NetType.NONE || netType == NetType.INVALIDATED) {
                                invoke(methodManager, key, netType)
                            }
                        }
                        NetType.CELLULAR -> {
                            if (netType == NetType.CELLULAR || netType == NetType.NONE || netType == NetType.INVALIDATED) {
                                invoke(methodManager, key, netType)
                            }
                        }
                        NetType.NONE -> {
                            invoke(methodManager, key, netType)
                        }
                        NetType.INVALIDATED -> {
                            invoke(methodManager, key, netType)
                        }
                        else -> {
                            Logger.d("其他")
                        }
                    }
                    break
                }
            }
        }
    }

    private fun invoke(
        methodManager: MethodManager,
        objects: Any,
        netType: NetType
    ) {
        methodManager.method.invoke(objects, netType)
    }

    fun register(objects: Any) {
        var methodList = networkList[objects]
        if (methodList.isNullOrEmpty()) {
            //收集容器中的方法
            methodList = findAnnotationMethod(objects)
            networkList[objects] = methodList
        }
    }

    private fun findAnnotationMethod(objects: Any): MutableList<MethodManager> {
        val methodList: MutableList<MethodManager> = mutableListOf()
        val methods = objects.javaClass.methods
        for (method in methods) {
            val network = method.getAnnotation(Network::class.java) ?: continue

            val parameterTypes = method.parameterTypes
            if (parameterTypes.isEmpty() || parameterTypes.size > 1) {
                continue
            }
            methodList.add(MethodManager(parameterTypes[0], network.netType, method))
        }
        return methodList
    }

}