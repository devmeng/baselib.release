package com.devmeng.baselib.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import com.devmeng.baselib.R
import com.devmeng.baselib.utils.Logger

/**
 * FileName: GuideCoverWidget
 * Author: 孟海粟
 * Date: 2021/10/9 15:29
 * Description:
 */
class NoviceGuideCover @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context, attrs, defStyleAttr
) {

    init {
        initConfigure()
    }

    private var mPaint: Paint? = null
    private var coveredBack: RectF? = null
    private var carvedPath: Path? = null
    private var isCircle = false
    private var carvedX = 0
    private var carvedY = 0
    private var carvedCircleRadius = 0
    private var carvedSquareLeft = 0
    private var carvedSquareRight = 0
    private var carvedSquareTop = 0
    private var carvedSquareBottom = 0

    private var mXfermode: PorterDuffXfermode? = null
    private var mWidthPixels = 0
    private var mWidth = 0
    private var mHeight = 0

    private var upX = 0
    private var upY = 0

    private var onCarvedClickListener: OnCarvedClickListener? = null

    private fun initConfigure() {
//        setCoverColor(getColor(R.color.color_trans_55000000));
        id = 0x200
        mXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = getColor(R.color.color_trans_40)
        carvedPath = Path()
        val displayMetrics = resources.displayMetrics
        mWidthPixels = displayMetrics.widthPixels

        circle(120, 120, 100)
    }

    fun circle(x: Int, y: Int, radius: Int) {
        carvedX = x
        carvedY = y
        carvedCircleRadius = radius
        carvedPath!!.addCircle(x.toFloat(), y.toFloat(), radius.toFloat(), Path.Direction.CW)
        onCarvedClickListener = null
        isCircle = true
        invalidate()
        visibility = VISIBLE
    }

    fun roundRectangle(left: Int, right: Int, top: Int, bottom: Int, rx: Int, ry: Int) {
        carvedX = rx
        carvedY = ry
        carvedSquareLeft = left
        carvedSquareTop = top
        carvedSquareRight = right
        carvedSquareBottom = bottom

        carvedPath!!.addRoundRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            bottom.toFloat(),
            rx.toFloat(),
            ry.toFloat(),
            Path.Direction.CW
        )
        isCircle = false
        invalidate()
        visibility = VISIBLE
    }

    fun clearGuide() {
        val disappearAnim = AlphaAnimation(1F, 0F)
        disappearAnim.duration = 200
//        this.animation = disappearAnim
        startAnimation(disappearAnim)

        visibility = GONE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            mWidth = mWidthPixels / 2
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            mHeight = 80
        }
        val displayMetrics = resources.displayMetrics
        Logger.e("displayMetrics.widthPixels => " + displayMetrics.widthPixels)
        Logger.e("displayMetrics.heightPixels => " + displayMetrics.heightPixels)
        setMeasuredDimension(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val displayMetrics = resources.displayMetrics
        //将绘制操作保存到新的图层，因为图像合成是很昂贵的操作，将用到硬件加速，这里将图像合成的处理放到离屏缓存中进行
        val width = canvas.width
        val height = canvas.height
        coveredBack = RectF(0F, 0F, width.toFloat(), height.toFloat())

        val saveCount = canvas.saveLayer(coveredBack!!, mPaint)

        Logger.e("canvas.getWidth() => $width")
        Logger.e("canvas.getHeight() => $height")
        //画遮罩的颜色
        canvas.drawRect(coveredBack!!, mPaint!!)
        //按 Path 来裁切
        canvas.clipPath(carvedPath!!)
        //画镂空的范围
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC_OUT)
        mPaint!!.xfermode = null
        //还原画布
        canvas.restoreToCount(saveCount)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_UP -> {
                upX = event.x.toInt()
                upY = event.y.toInt()
                Logger.e("upX -> $upX")
                Logger.e("upY -> $upY")
                judgeCarvedLocation(upX, upY)
            }
        }
        return true
    }

    private fun judgeCarvedLocation(upX: Int, upY: Int): Boolean {
        if (isCircle) {
            if ((upX > carvedX - carvedCircleRadius).and(upX < carvedX + carvedCircleRadius)) {
                if ((upY > carvedY - carvedCircleRadius).and(upY < carvedY + carvedCircleRadius)) {
                    clearGuide()
//                    onCarvedClickListener!!.onCarvedClick()
                    return true
                }
            }
            return false
        }
        if ((upX > carvedSquareLeft).and(upX < carvedSquareRight)) {
            if ((upY > carvedSquareTop).and(upY < carvedSquareBottom)) {
                clearGuide()
//                onCarvedClickListener!!.onCarvedClick()
                return true
            }
        }
        return false
    }

    private fun getColor(color: Int): Int {
        return context.getColor(color)
    }

    /**
     * 镂空处点击事件接口
     */
    interface OnCarvedClickListener {
        fun onCarvedClick()
    }
}