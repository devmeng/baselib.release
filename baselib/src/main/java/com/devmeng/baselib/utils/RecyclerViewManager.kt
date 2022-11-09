package com.devmeng.baselib.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devmeng.baselib.utils.RecyclerViewManager.Companion.configGridLayoutManager
import com.devmeng.baselib.utils.RecyclerViewManager.Companion.configLinearLayoutManager
import com.devmeng.baselib.utils.RecyclerViewManager.Companion.configStaggeredGridLayoutManager

/**
 * Created by Richard
 * Version : 1
 * Description :
 * RecyclerView 管理器
 * @see configLinearLayoutManager 列表设置线性布局
 * @see configGridLayoutManager 列表设置网格布局
 * @see configStaggeredGridLayoutManager 列表设置瀑布流布局
 */
class RecyclerViewManager {

    companion object {

        /**
         * 线性布局管理器
         * @param recyclerView 列表
         * @param orientation 子条目摆放方向
         * @param reverseLayout 列表是否倒置
         */
        fun configLinearLayoutManager(
            recyclerView: RecyclerView,
            orientation: Int = RecyclerView.VERTICAL,
            reverseLayout: Boolean = false
        ) {
            recyclerView.layoutManager =
                LinearLayoutManager(
                    recyclerView.context.applicationContext,
                    orientation,
                    reverseLayout
                )
        }

        /**
         * 网格布局管理器
         * @param recyclerView 列表
         * @param spanCount 一行排列的个数
         * @param orientation 子条目摆放方向
         * @param reverseLayout 列表是否倒置
         */
        fun configGridLayoutManager(
            recyclerView: RecyclerView, spanCount: Int = 2,
            orientation: Int = RecyclerView.VERTICAL,
            reverseLayout: Boolean = false
        ) {
            recyclerView.layoutManager =
                GridLayoutManager(
                    recyclerView.context.applicationContext,
                    spanCount,
                    orientation,
                    reverseLayout
                )
        }


        /**
         * 瀑布流布局管理器
         * @param recyclerView 列表
         * @param spanCount 一行排列的个数
         * @param orientation 子条目摆放方向
         */
        fun configStaggeredGridLayoutManager(
            recyclerView: RecyclerView, spanCount: Int = 2,
            orientation: Int = RecyclerView.VERTICAL,
        ) {
            recyclerView.layoutManager =
                StaggeredGridLayoutManager(spanCount, orientation)
        }
    }

}