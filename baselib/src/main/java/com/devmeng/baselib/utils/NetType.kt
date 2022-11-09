package com.devmeng.baselib.utils

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
enum class NetType {

    //有网络, wifi or gprs
    AUTO,

    //wifi
    WIFI,

    //主要用于 PC / 笔记本电脑 等 / PDA 设备
    CMNET,

    //手机数据流量
    CELLULAR,

    //无网络
    NONE,

    //网络不可用
    INVALIDATED,

}