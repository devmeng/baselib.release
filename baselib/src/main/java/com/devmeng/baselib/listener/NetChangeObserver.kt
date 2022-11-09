package com.devmeng.baselib.listener

import com.devmeng.baselib.utils.NetType

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
interface NetChangeObserver {

    fun onConnected(netType: NetType)

    fun onDisConnect()

}