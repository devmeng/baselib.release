package com.devmeng.baselib.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  18:09
 * Version : 1
 */
abstract class BaseAdapter<T>(
    var context: Context,
    var type: Int
) :
    RecyclerView.Adapter<BaseViewHolder>() {

    var mList: MutableList<T>? = ArrayList()

    var mOnItemClickListener: BaseViewHolder.OnItemClickListener<T>? = null

    var mOnItemViewClickListener: BaseViewHolder.OnItemViewClickListener<T>? = null

    var mOnItemLongClickListener: BaseViewHolder.OnItemLongClickListener<T>? = null

    var mOnItemViewLongClickListener: BaseViewHolder.OnItemViewLongClickListener<T>? = null

    abstract fun getItemLayoutId(): Int

    abstract fun bind(holder: BaseViewHolder, itemData: T, position: Int)

    override fun getItemCount(): Int {
        return mList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(parent.context.applicationContext)
                .inflate(getItemLayoutId(), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val itemData = mList!![position]
        setOnItemClickListener(holder, itemData, position)
        bind(holder, itemData, position)
    }

    fun refreshAdapter(list: MutableList<T>) {
        refreshAdapter(list, true, list.size)
    }

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
                    var preSize = size
                    addAll(list)
                    notifyItemRangeInserted(preSize, pageSize)
                }
            }
        }
    }

    open fun reverseData(list: MutableList<T>) {
        list.reverse()
        with(mList!!) {
            clear()
            addAll(list)
        }
        notifyDataSetChanged()
    }

    open fun addItem(itemData: T, isReverse: Boolean) {
        with(mList!!) {
            when (isReverse) {
                isReverse -> {
                    add(0, itemData)
                    notifyItemRangeInserted(0, 1)
                }
                else -> {
                    val preSize = size
                    add(itemData)
                    notifyItemRangeInserted(preSize, 1)
                }
            }
        }
        notifyDataSetChanged()
    }

    open fun removeItem(position: Int) {
        mList!!.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }

    open fun clearAdapter() {
        mList!!.clear()
        notifyDataSetChanged()
    }

    private fun setOnItemClickListener(holder: BaseViewHolder, itemData: T, position: Int) {

        with(holder) {
//            Logger.d("OnItemClickListener -> $mOnItemClickListener")
            itemView.setOnClickListener {
                mOnItemClickListener!!.onItemClick(holder, itemData, position)
            }
        }
    }

    fun setOnItemLongClickListener(holder: BaseViewHolder, itemData: T, position: Int) {

        with(holder) {
//            Logger.d("mOnItemLongClickListener -> $mOnItemLongClickListener")
            itemView.setOnLongClickListener(View.OnLongClickListener {
                mOnItemLongClickListener!!.onLongClick(holder, itemData, position)
                return@OnLongClickListener true
            })
        }
    }

    fun setOnItemViewClickListener(holder: BaseViewHolder, view: View, itemData: T, position: Int) {
        with(view) {
//            Logger.d("mOnItemViewClickListener -> $mOnItemViewClickListener")
            setOnClickListener {
                mOnItemViewClickListener!!.onViewClick(holder, view, itemData, position)
            }
        }
    }

    fun setOnItemViewLongClickListener(
        holder: BaseViewHolder,
        view: View,
        itemData: T,
        position: Int
    ) {
        with(view) {
//            Logger.d("mOnItemViewLongClickListener -> $mOnItemViewLongClickListener")
            setOnLongClickListener(View.OnLongClickListener {
                mOnItemViewLongClickListener!!.onViewLongClick(holder, view, itemData, position)
                return@OnLongClickListener true
            })
        }
    }

}