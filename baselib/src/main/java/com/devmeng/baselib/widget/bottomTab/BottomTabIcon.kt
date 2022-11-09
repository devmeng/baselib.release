package com.devmeng.baselib.widget.bottomTab

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.widget.Checkable
import androidx.annotation.DrawableRes
import com.airbnb.lottie.LottieAnimationView
import com.devmeng.baselib.R
import com.devmeng.baselib.utils.LottieBuilder

/**
 * Created by Richard -> MHS
 * Date : 2022/7/26  18:37
 * Version : 1
 * Description :
 */
class BottomTabIcon @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LottieAnimationView(context, attrs), Checkable {


    private val stateSelected = intArrayOf(android.R.attr.state_selected)
    private var isChecked = false
    private var lottieIconBuilder: LottieBuilder.Builder? = null
    var iconType: Int = 0

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) {
            mergeDrawableStates(drawableState, stateSelected)
        }
        return drawableState
    }

    init {
        lottieIconBuilder = LottieBuilder.builder()!!
            .with(context)
            .lottie(this)
            .clear()
    }

    fun setStaticIcon(@DrawableRes iconRes: Int) {
        lottieIconBuilder!!
            .imageResource(iconRes)
    }

    fun setDynamicIcon(targetDirName: String) {
        lottieIconBuilder!!
            .folderName(targetDirName)
            .jsonAssetsName()
    }

    fun setIconTint() {
        lottieIconBuilder!!
            .imageTint(getColor(R.color.selector_gray_select_black_333))
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            ACTION_UP -> {
                toggle()
            }
        }
        return true
    }

    override fun setChecked(checked: Boolean) {
        if (isChecked != checked) {
            isChecked = checked
        }
        if (iconType == BottomTabItem.ICON_STATICS) {
            refreshDrawableState()
            return
        }
        lottieIconBuilder!!.play()
    }

    override fun isChecked(): Boolean = isChecked

    override fun toggle() {
        setChecked(!isChecked)
    }

    private fun getColor(color: Int): Int = context.getColor(color)

}