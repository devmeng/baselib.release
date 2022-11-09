package com.devmeng.baselib.annotation

import com.devmeng.baselib.utils.NetType

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Network(val netType: NetType = NetType.AUTO)
