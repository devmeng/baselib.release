package com.devmeng.baselib.base.bind

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
@SuppressLint("NotifyDataSetChanged")
abstract class BaseBindAdapter<T : Any> :
    RecyclerView.Adapter<BaseBindViewHolder>() {

    abstract fun bind(holder: BaseBindViewHolder, itemData: T, position: Int)
    abstract fun getItemViewBindingRoot(parent: ViewGroup): View

    private var mList: MutableList<T>? = arrayListOf()

    var onItemClickListener: BaseBindViewHolder.OnItemClickListener<T>? = null
    var onItemViewClickListener: BaseBindViewHolder.OnItemViewClickListener<T>? = null
    var onItemLongClickListener: BaseBindViewHolder.OnItemLongClickListener<T>? = null
    var onItemViewLongClickListener:
            BaseBindViewHolder.OnItemViewLongClickListener<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindViewHolder {
        return BaseBindViewHolder(getItemViewBindingRoot(parent))
    }

    override fun onBindViewHolder(holder: BaseBindViewHolder, position: Int) {
        val itemData = mList!![position]
        setOnItemClickListener(holder, itemData, position)
        bind(holder, itemData, position)
    }

    override fun getItemCount(): Int {
        return mList!!.size
    }

    /**
     * 刷新适配器，更新列表数据
     * @param list 列表数据集合
     */
    fun refreshAdapter(list: MutableList<T>) {
        refreshAdapter(list, true, list.size)
    }

    /**
     * 刷新适配器，更新列表数据
     * @param list 列表数据集合
     * @param first 是否为第一次加载
     * @param pageSize 单页数据量
     */
    fun refreshAdapter(list: MutableList<T>, first: Boolean, pageSize: Int) {
        with(mList!!) {
            when {
                //第一次添加
                first -> {
                    clear()
                    addAll(list)
                    notifyDataSetChanged()
                }
                else -> {
                    val preSize = size
                    addAll(list)
                    notifyItemRangeInserted(preSize, pageSize)
                }
            }
        }
    }

    /**
     * 倒置列表数据
     * @param list 需要倒置的数据集合
     */
    fun reverseData(list: MutableList<T>) {
        list.reverse()
        with(mList!!) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    /**
     * 列表添加子条目
     * @param itemData 实体类数据
     * @param isReverse 是否倒置
     */
    fun addItem(itemData: T, isReverse: Boolean) {
        with(mList!!) {
            when (isReverse) {
                true -> {
                    add(0, itemData)
                    notifyItemRangeInserted(0, 1)
                }
                false -> {
                    val preSize = size
                    add(itemData)
                    notifyItemRangeInserted(preSize, 1)
                }
            }
        }
        notifyDataSetChanged()
    }

    /**
     * 移除子条目
     * @param position 需移除子条目的下标
     */
    fun removeItem(position: Int) {
        mList!!.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    /**
     * 清理适配器，即清除列表子条目
     */
    fun clearAdapter() {
        mList!!.clear()
        notifyDataSetChanged()
    }

    /**
     * 设置子条目点击事件
     * @param holder
     * @param itemData 当前点击的数据实体类
     * @param position 当前点击子条目下标
     */
    fun setOnItemClickListener(holder: BaseBindViewHolder, itemData: T, position: Int) {
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(itemData, position)
        }
    }

    /**
     * 设置子条目长按事件
     * @param holder
     * @param itemData 当前长按的数据实体类
     * @param position 当前长按子条目下标
     */
    fun setOnItemLongClickListener(holder: BaseBindViewHolder, itemData: T, position: Int) {
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.onLongClick(itemData, position)
            true
        }
    }

    /**
     * 设置子条目子控件的点击事件
     * @param view 点击的控件
     * @param itemData 当前点击的数据实体类
     * @param position 当前点击子条目下标
     */
    fun setOnItemViewClickListener(view: View, itemData: T, position: Int) {
        view.setOnClickListener {
            onItemViewClickListener?.onViewClick(view, itemData, position)
        }
    }

    /**
     * 设置子条目子控件的长按事件
     * @param view 长按的控件
     * @param itemData 当前长按的数据实体类
     * @param position 当前长按子条目下标
     */
    fun setOnItemViewLongClickListener(view: View, itemData: T, position: Int) {
        view.setOnLongClickListener {
            onItemViewLongClickListener?.onViewLongClick(view, itemData, position)
            true
        }
    }

}