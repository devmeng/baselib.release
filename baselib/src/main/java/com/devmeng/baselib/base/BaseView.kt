package com.devmeng.baselib.base

import android.content.Context

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  10:27
 * Version : 1
 */
interface BaseView {

    /**
     * 显示加载框
     */
    fun showLoading()

    /**
     * 隐藏加载框
     */
    fun hideLoading()

    /**
     * 获取上下文
     */
    fun getViewContext(): Context

}