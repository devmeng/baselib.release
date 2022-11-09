package com.devmeng.baselib.utils

/**
 * Created by Richard -> MHS
 * Date : 2022/7/19  9:47
 * Version : 1
 * Description :
 */
class BitmapUtils {

    companion object {
        var bitmapUtils: BitmapUtils? = null
        fun instance(): BitmapUtils {
            if (bitmapUtils == null) {
                bitmapUtils = BitmapUtils()
            }
            return bitmapUtils as BitmapUtils
        }
    }

}