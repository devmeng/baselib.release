package com.devmeng.baselib.widget.bottomTab

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.devmeng.baselib.R

/**
 * Created by Richard -> MHS
 * Date : 2022/7/25  16:23
 * Version : 1
 * Description :
 */
class BottomTabItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr) {

    companion object {
        const val ICON_DYNAMIC = 0
        const val ICON_STATICS = 1
    }

    private val tabIcon = BottomTabIcon(context)
    private val tabTitle = TextView(context)
    private var itemSelected = false

    var itemSelectedColor = getColor(R.color.color_black_333)
    var itemUnSelectedColor = getColor(R.color.color_gray_6C6C6C)
    var iconType = ICON_DYNAMIC
        set(value) {
            field = value
            tabIcon.iconType = value
        }

    init {
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

    }

    fun setIcon(@DrawableRes icon: Int) {
        tabIcon.iconType = ICON_STATICS
        tabIcon.setStaticIcon(icon)
    }

    fun setUnSelectedIcon(targetDirName: String) {
        tabIcon.setDynamicIcon(targetDirName)
    }

    fun setSelectedIcon(targetDirName: String) {
        tabIcon.setDynamicIcon(targetDirName)
    }

    fun setTitle(title: String) {
        tabTitle.text = title
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (isSelected != selected) {
            isSelected = selected
        }
        if (isSelected) {
            tabTitle.setTextColor(itemSelectedColor)
        } else {
            tabTitle.setTextColor(itemUnSelectedColor)
        }
        tabIcon.isChecked = isSelected

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            ACTION_UP -> {
            }
        }
        return true
    }

    private fun getColor(@ColorRes color: Int): Int = context.getColor(color)

}