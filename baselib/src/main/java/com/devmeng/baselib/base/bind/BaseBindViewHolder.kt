package com.devmeng.baselib.base.bind

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

/**
 * Created by Richard
 * Version : 1
 * Description :
 */
open class BaseBindViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    open fun setVisible(view: View, isShow: Boolean) {
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

    interface OnItemClickListener<T> {
        fun onItemClick(itemData: T, position: Int)
    }

    interface OnItemViewClickListener<T> {
        fun onViewClick(view: View, itemData: T, position: Int)
    }

    interface OnItemLongClickListener<T> {
        fun onLongClick(itemData: T, position: Int)
    }

    interface OnItemViewLongClickListener<T> {
        fun onViewLongClick(view: View, itemData: T, position: Int)
    }


}