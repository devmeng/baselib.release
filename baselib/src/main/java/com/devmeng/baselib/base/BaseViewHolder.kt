package com.devmeng.baselib.base

import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.devmeng.baselib.utils.EMPTY

/**
 * Created by Richard -> MHS
 * Date : 2022/5/30  18:10
 * Version : 1
 */
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var viewArray: SparseArray<View> = SparseArray()

    open fun setCircleImage(@IdRes id: Int, url: Any) {
        setCircleImage(getView(id), url)
    }

    open fun setCornerImage(@IdRes id: Int, radius: Int, url: Any) {
        setCornerImage(getView(id), radius, url)
    }

    open fun setCircleImage(view: View, url: Any) {
        if (view !is ImageView) {
            throw IllegalStateException("请使用 ImageView 组件", Throwable().cause)
        }
        Glide.with(view.context.applicationContext)
            .applyDefaultRequestOptions(RequestOptions().circleCrop())
            .asFile()
            .load(url)
            .into(view)
    }

    open fun setCornerImage(view: View, radius: Int, url: Any) {
        if (view !is ImageView) {
            throw IllegalStateException("请使用 ImageView 组件", Throwable().cause)
        }
        Glide.with(view.context.applicationContext)
            .applyDefaultRequestOptions(RequestOptions.bitmapTransform(RoundedCorners(radius)))
            .asFile()
            .load(url)
            .into(view)
    }

    open fun setText(@IdRes id: Int, text: String) {
        getView<TextView>(id).text = text
    }

    open fun setText(@IdRes id: Int, @StringRes strId: Int) {
        setText(id, strId, EMPTY)
    }

    open fun setText(@IdRes id: Int, @StringRes strId: Int, extraStr: String) {
        val view = getView<TextView>(id)
        view.text = view.context.applicationContext.getString(strId, extraStr)
    }

    open fun setVisible(view: View, isShow: Boolean) {
        setVisible(view.id, isShow)
    }

    open fun setVisible(@IdRes id: Int, isShow: Boolean) {
        val view: View = getView(id)
        with(view) {
            visibility = when (isShow) {
                true -> {
                    View.VISIBLE
                }
                false -> {
                    View.GONE
                }
            }
        }
    }

    open fun <V : View> getView(@IdRes id: Int): V {
        var view = viewArray.get(id)
        if (view == null) {
            view = itemView.findViewById<V>(id)
            viewArray.put(id, view)
        }
        return view as V
    }

    interface OnItemClickListener<T> {
        fun onItemClick(holder: BaseViewHolder, itemData: T, position: Int)
    }

    interface OnItemViewClickListener<T> {
        fun onViewClick(holder: BaseViewHolder, view: View, itemData: T, position: Int)
    }

    interface OnItemLongClickListener<T> {
        fun onLongClick(holder: BaseViewHolder, itemData: T, position: Int)
    }

    interface OnItemViewLongClickListener<T> {
        fun onViewLongClick(holder: BaseViewHolder, view: View, itemData: T, position: Int)
    }

}