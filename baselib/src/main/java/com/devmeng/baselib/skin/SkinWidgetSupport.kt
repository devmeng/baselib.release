package com.devmeng.baselib.skin

import com.devmeng.baselib.skin.entity.SkinPair

/**
 * Created by Richard -> MHS
 * Date : 2022/10/25  17:41
 * Version : 1
 */
interface SkinWidgetSupport {
    val attrsList: List<String>
    fun applySkin(pairList: List<SkinPair>)
}