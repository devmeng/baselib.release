package com.devmeng.baselib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.GravityInt
import com.devmeng.baselib.databinding.LayoutToastViewBinding

/**
 * Created by Richard -> MHS
 * Date : 2022/7/7  19:08
 * Version : 1
 * Description :
 */
open class ToastView(var context: Context) : Toast(context) {

    private val handler = Handler(context.mainLooper) {
        view = inflate.root
        show()
        false
    }

    val inflate = LayoutToastViewBinding.inflate(LayoutInflater.from(context), null, false)

    open fun showText(msg: CharSequence) = showText(msg, Gravity.BOTTOM)

    open fun showText(msg: CharSequence, offsetX: Float, offsetY: Float) =
        showText(msg, Gravity.BOTTOM, offsetX.toInt(), offsetY.toInt())

    open fun showText(msg: CharSequence, @GravityInt gravity: Int) =
        showText(msg, gravity, LENGTH_SHORT)

    open fun showText(msg: CharSequence, @GravityInt gravity: Int, time: Int) {
        showText(msg, gravity, 0, 0)
    }

    open fun showText(msg: CharSequence, @GravityInt gravity: Int, offsetX: Int, offsetY: Int) {
        showText(msg, gravity, LENGTH_SHORT, offsetX, offsetY)
    }

    open fun showText(
        msg: CharSequence,
        @GravityInt gravity: Int,
        time: Int,
        offsetX: Int,
        offsetY: Int
    ) {
        showToastGravity(msg, gravity, time, offsetX, offsetY)
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun showToastGravity(
        msg: CharSequence,
        @GravityInt gravity: Int,
        time: Int,
        offsetX: Int,
        offsetY: Int
    ) {
        inflate.tvToastViewMessage.text = msg
//        toastView = ToastView(context)
        duration = time
        setGravity(gravity, offsetX, offsetY)

        handler.sendEmptyMessage(0)
    }

}