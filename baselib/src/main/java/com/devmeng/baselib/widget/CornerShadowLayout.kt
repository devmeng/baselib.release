package com.devmeng.baselib.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.devmeng.baselib.R
import com.devmeng.baselib.skin.SkinWidgetSupport
import com.devmeng.baselib.skin.entity.SkinPair
import com.devmeng.baselib.skin.utils.SkinResources
import com.devmeng.baselib.utils.Logger

/**
 * Created by Richard -> MHS
 * Date : 2022/6/12  16:54
 * Version : 2
 * Description : 圆角阴影布局
 *
 * ↓ 自定义属性 ↓
 * shadowColor: 阴影颜色
 * shadowRadius: 阴影半径
 * allCornerRadius: 所有圆角半径
 * topLeftRadius: 左上圆角半径
 * topRightRadius: 右上圆角半径
 * bottomLeftRadius: 左下圆角半径
 * bottomRightRadius: 右下圆角半径
 * backRes: 设置背景图片资源 ID（后期更新）
 * backColor: 圆角背景颜色
 * borderColor: 边框颜色
 * borderWidth: 边框宽度
 */
@SuppressLint("ResourceAsColor")
class CornerShadowLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), SkinWidgetSupport {

    override val attrsList: List<String> = listOf(
        "shadeColor",
        "shadeRadius",
        "allCornerRadius",
        "topLeftRadius",
        "topRightRadius",
        "bottomLeftRadius",
        "bottomRightRadius",
        "backRes",
        "backColor",
        "borderColor",
        "borderWidth",
    )

    private var widthMode: Int = 0
    private var heightMode: Int = 0
    private var widthSize: Int = 0
    private var heightSize: Int = 0
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    //自定义属性
    private var padding: Int = 0
    private var paddingVertical: Int = 0
    private var paddingHorizontal: Int = 0
    private var borderWidth: Float = 0F
    private var shadeRadius: Float = 0F
    private var allCornerRadius: Float = 0F
    private var topLeftRadius: Float = 0F
    private var topRightRadius: Float = 0F
    private var bottomLeftRadius: Float = 0F
    private var bottomRightRadius: Float = 0F
    private var backRes: Int = 0
    private var borderColor: Int = R.color.color_black_333
    private var backColor: Int = R.color.color_black_333
    private var shadeColor: Int = R.color.black

    //画笔
    private var cornerBackPaint: Paint? = null
    private var borderPaint: Paint? = null

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CornerShadowLayout)

        with(typedArray) {

            //内边距
            padding = getDimension(
                R.styleable.CornerShadowLayout_android_padding, padding.toFloat()
            ).toInt()
            paddingHorizontal = getDimension(
                R.styleable.CornerShadowLayout_android_paddingHorizontal,
                paddingHorizontal.toFloat()
            ).toInt()
            paddingVertical = getDimension(
                R.styleable.CornerShadowLayout_android_paddingVertical, paddingVertical.toFloat()
            ).toInt()

            //背景相关
            backRes =
                getResourceId(R.styleable.CornerShadowLayout_backRes, backRes)
            backColor =
                getColor(
                    R.styleable.CornerShadowLayout_backColor,
                    getColor(R.color.color_white_FFF)
                )

            //布局边框相关
            borderWidth = getDimension(
                R.styleable.CornerShadowLayout_borderWidth,
                borderWidth
            )
            borderColor = getColor(
                R.styleable.CornerShadowLayout_borderColor,
                0
            )

            //布局阴影相关
            shadeRadius = getDimension(
                R.styleable.CornerShadowLayout_shadeRadius,
                0F
            )
            shadeColor =
                getColor(
                    R.styleable.CornerShadowLayout_shadeColor,
                    0
                )
            //圆角相关
            allCornerRadius = getDimension(
                R.styleable.CornerShadowLayout_allCornerRadius,
                allCornerRadius
            )
            topLeftRadius = getDimension(
                R.styleable.CornerShadowLayout_topLeftRadius,
                topLeftRadius
            )
            topRightRadius = getDimension(
                R.styleable.CornerShadowLayout_topRightRadius,
                topRightRadius
            )
            bottomLeftRadius = getDimension(
                R.styleable.CornerShadowLayout_bottomLeftRadius,
                bottomLeftRadius
            )
            bottomRightRadius = getDimension(
                R.styleable.CornerShadowLayout_bottomRightRadius,
                bottomRightRadius
            )

            recycle()
        }
        initConfig()
        setWillNotDraw(false)
    }

    @SuppressLint("ResourceAsColor")
    private fun initConfig() {
        if (allCornerRadius != 0F) {
            topLeftRadius = allCornerRadius
            topRightRadius = allCornerRadius
            bottomLeftRadius = allCornerRadius
            bottomRightRadius = allCornerRadius
        }
        //背景画笔
        cornerBackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(cornerBackPaint!!) {
            color = backColor
            style = Paint.Style.FILL_AND_STROKE
            setShadowLayer(
                shadeRadius,
                0F,
                0F,
                shadeColor
            )
        }

        //边框画笔
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        with(borderPaint!!) {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }

        //必须关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        //设置背景
        if (backRes == 0) {
            return
        }
        setBackgroundResource(backRes)
    }

    private fun getRadiusArray(): FloatArray {
        return floatArrayOf(
            topLeftRadius,
            topLeftRadius,
            topRightRadius,
            topRightRadius,
            bottomRightRadius,
            bottomRightRadius,
            bottomLeftRadius,
            bottomLeftRadius,
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthMode = MeasureSpec.getMode(widthMeasureSpec)
        heightMode = MeasureSpec.getMode(heightMeasureSpec)
        widthSize = MeasureSpec.getSize(widthMeasureSpec)
        heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthOffset = widthOffsetConfig(shadeRadius.toInt() * 2)
        val heightOffset = heightOffsetConfig(shadeRadius.toInt() * 2)

        when (widthMode) {
            MeasureSpec.EXACTLY -> {
                mWidth = widthSize + widthOffset
                mHeight = calcHeight() + heightOffset
            }
            MeasureSpec.AT_MOST -> {
                mWidth =
                    (measuredWidth + shadeRadius.toInt() * 2)
                        .coerceAtMost(resources.displayMetrics.widthPixels) + widthOffset
                mHeight = calcHeight() + heightOffset
            }
            else -> {
                mWidth =
                    (measuredWidth + shadeRadius.toInt() * 2)
                        .coerceAtMost(resources.displayMetrics.widthPixels) + widthOffset
                mHeight = calcHeight() + heightOffset
            }
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val offset =
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX,
                shadeRadius,
                resources.displayMetrics
            ).toInt() * 2
        val widthOffset = widthOffsetConfig(offset)

        val heightOffset = heightOffsetConfig(offset)

        Logger.d("widthMode [$widthMode]  heightMode [$heightMode]")

        if (widthMode == MeasureSpec.AT_MOST) {
            when (heightMode) {
                MeasureSpec.AT_MOST -> {
                    offsetChildOutside(offset, offset, offset, offset)
                }
                MeasureSpec.EXACTLY -> {
                    offsetChildOutside(offset, heightOffset / 2, offset, heightOffset / 2)
                }
                else -> {
                    offsetChildOutside(offset, 0, offset, 0)
                }
            }
        } else if (widthMode == MeasureSpec.EXACTLY) {
            when (heightMode) {
                MeasureSpec.AT_MOST -> {
                    offsetChildOutside(widthOffset / 2, offset, widthOffset / 2, offset)
                }
                MeasureSpec.EXACTLY -> {
                    offsetChildOutside(
                        widthOffset / 2,
                        heightOffset / 2,
                        widthOffset / 2,
                        heightOffset / 2
                    )
                }
                else -> {
                    offsetChildOutside(offset, 0, offset, 0)
                }
            }
        } else {
            if (heightMode == MeasureSpec.AT_MOST) {
                offsetChildOutside(0, offset, 0, offset)
            }
        }

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        with(canvas!!) {
            val offset = (borderWidth + shadeRadius)
            val bgRectF = RectF(
                offset,
                offset,
                mWidth - offset,
                mHeight - offset
            )
            val bgPath = Path()
            bgPath.addRoundRect(bgRectF, getRadiusArray(), Path.Direction.CW)
            drawPath(bgPath, cornerBackPaint!!)

            val borderRectF = RectF(
                offset,
                offset,
                mWidth - offset,
                mHeight - offset
            )

            val borderPath = Path()
            borderPath.addRoundRect(borderRectF, getRadiusArray(), Path.Direction.CW)
            drawPath(borderPath, borderPaint!!)

        }
    }

    private fun widthOffsetConfig(offset: Int): Int {
        val screenWidth = resources.displayMetrics.widthPixels
        if (widthMode == MeasureSpec.EXACTLY) {
            if (widthSize == screenWidth) {
                return 0
            }
        }
        return offset
    }

    private fun heightOffsetConfig(offset: Int): Int {
        val screenHeight = resources.displayMetrics.heightPixels
        if (heightMode == MeasureSpec.EXACTLY) {
            if (heightSize == screenHeight) {
                return 0
            }
        }
        return offset
    }

    private fun offsetChildOutside(left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            Logger.d("top [${view.top}] left [${view.left}] right [${view.right}] bottom [${view.bottom}]")
            view.layout(
                view.left + left,
                view.top + top,
                view.right + right,
                view.bottom + bottom
            )
        }
    }

    private fun calcHeight(): Int =
        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            (measuredHeight + shadeRadius.toInt() * 2).coerceAtMost(heightSize)
        }

    private fun widthPadding() = when {
        padding != 0 -> padding * 2
        paddingHorizontal != 0 -> paddingHorizontal * 2
        else -> 0
    }

    private fun heightPadding() = when {
        padding != 0 -> padding * 2
        paddingVertical != 0 -> paddingVertical * 2
        else -> 0
    }

    private fun marginHorizontalOffset(): Int {
        var marginHorizontalOffset = 0
        if (marginStart != 0) {
            marginHorizontalOffset += marginStart
        }
        if (marginEnd != 0) {
            marginHorizontalOffset += marginEnd
        }
        return marginHorizontalOffset
    }

    private fun marginVerticalOffset(): Int {
        var marginVerticalOffset = 0
        if (marginTop != 0) {
            marginVerticalOffset += marginTop
        }
        if (marginBottom != 0) {
            marginVerticalOffset += marginBottom
        }
        return marginVerticalOffset
    }

    private fun getColor(color: Int) = context.getColor(color)

    /**
     * 释放资源
     * 1.取消关闭硬件加速
     */
    fun release() {
        //todo other
        setLayerType(LAYER_TYPE_NONE, null)
    }

    /**
     * 应用自定义 View 的皮肤包
     */
    override fun applySkin(pairList: List<SkinPair>) {
        for ((attrName, resId) in pairList) {
            Logger.d("attrName -> [$attrName] resId -> [$resId]")
            when (attrName) {
                "shadeColor" -> {
                    shadeColor = SkinResources.instance.getColor(resId)
                }
                "shadeRadius" -> shadeRadius = SkinResources.instance.getDimension(resId)
                "allCornerRadius" -> {
                    allCornerRadius = SkinResources.instance.getDimension(resId)
                }
                "topLeftRadius" -> {
                    topLeftRadius = SkinResources.instance.getDimension(resId)
                }
                "topRightRadius" -> {
                    topRightRadius = SkinResources.instance.getDimension(resId)
                }
                "bottomLeftRadius" -> {
                    bottomLeftRadius = SkinResources.instance.getDimension(resId)
                }
                "bottomRightRadius" -> {
                    bottomRightRadius = SkinResources.instance.getDimension(resId)
                }
                "backRes" -> {
                    TODO()
                }
                "backColor" -> {
                    backColor = SkinResources.instance.getColor(resId)
                }
                "borderColor" -> {
                    borderColor = SkinResources.instance.getColor(resId)
                }
                "borderWidth" -> {
                    borderWidth = SkinResources.instance.getDimension(resId)
                }
            }
        }
        //重新为画笔的阴影上色及量宽
        initConfig()
        invalidate()
    }

}