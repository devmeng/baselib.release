package com.devmeng.baselib.utils

/**
 * Created by William -> MHS
 * Date : 2022.04.11  17:41
 * Version : 1
 */
class DigitalConvertUtils {

    fun byte2mb(byteContent: Float): Float {
        //byte => KB
        val kb = byteContent / 1024
        //kb => mb
        return kb / 1024
    }

    companion object {
        private var sDigitalConvertUtils: DigitalConvertUtils? = null
        val instance: DigitalConvertUtils?
            get() {
                if (sDigitalConvertUtils == null) {
                    sDigitalConvertUtils = DigitalConvertUtils()
                }
                return sDigitalConvertUtils
            }
    }
}