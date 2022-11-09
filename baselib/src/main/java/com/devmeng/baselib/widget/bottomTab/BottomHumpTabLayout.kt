package com.devmeng.baselib.widget.bottomTab

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.devmeng.baselib.R
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

/**
 * Created by Richard -> MHS
 * Date : 2022/7/17  16:48
 * Version : 1
 * Description :
 */
class BottomHumpTabLayout : ConstraintLayout {

    //自定义属性
    private var humpRadius = 0F
    private var humpCornerRadius = 0F
    private var humpLocation = HUMP_CENTER
    private var humpLocationOffsetX = 0F
    private var humpIcon = 0
    private var humpIconSize = 0F
    private var humpAngle = 150F
    private var layoutShadowRadius = 10F
    private var shadowColor = getColor(R.color.color_black_333)


    private var mWidth = resources.displayMetrics.widthPixels
    private var mHeight = 0F
    private var layoutHeight = 0F
    private var arcAngle = 0.0

    private var humpRx = 0F
    private var humpRy = 0F

    //凸起画笔
    private var humpPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //容器画笔
    private var layoutPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    //容器画笔
    private var bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val layoutPath: Path = Path()
    private var layoutColor = getColor(R.color.color_white_FFF)
    private var topLeftRadius: Float = 0F
    private var topRightRadius: Float = 0F
    private var childViewWidth = 0

    private var itemTextArr = initItemTextArray()
    private var itemIconArr = initItemIconArray()

    companion object {
        const val HUMP_CENTER = 0
        const val HUMP_START = 1
        const val HUMP_END = 2
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("CustomViewStyleable")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.BottomTabLayout)
        with(typedArray) {

            //凸起半径
            humpRadius = getDimension(
                R.styleable.BottomTabLayout_humpRadius,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    42F,
                    resources.displayMetrics
                )
            )

            //凸起圆角半径
            humpCornerRadius =
                getDimension(R.styleable.BottomTabLayout_humpCornerRadius, humpCornerRadius)

            //凸起位置
            humpLocation = getInt(R.styleable.BottomTabLayout_humpLocation, humpLocation)

            //凸起位置偏移量
            humpLocationOffsetX =
                getDimension(R.styleable.BottomTabLayout_humpLocationOffsetX, humpLocationOffsetX)

            //凸起位置图片
            humpIcon = getResourceId(R.styleable.BottomTabLayout_humpIcon, humpIcon)

            //凸起位置图片尺寸
            humpIconSize = getDimension(
                R.styleable.BottomTabLayout_humpIconSize,
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    60F,
                    resources.displayMetrics
                )
            )

            //容器颜色
            layoutColor = getColor(R.styleable.BottomTabLayout_layoutColor, layoutColor)

            //阴影颜色
            shadowColor = getColor(R.styleable.BottomTabLayout_shaderColor, shadowColor)

            //阴影半径
            layoutShadowRadius =
                getDimension(R.styleable.BottomTabLayout_shaderRadius, layoutShadowRadius)

            recycle()
        }

        initConfig()
    }

    private fun initConfig() {
        with(humpPaint) {
            color = layoutColor
            style = Paint.Style.FILL_AND_STROKE
        }
        with(layoutPaint) {
            color = layoutColor
            style = Paint.Style.FILL_AND_STROKE
        }
        with(bitmapPaint) {
            color = getColor(R.color.color_white_FFF)
            style = Paint.Style.FILL_AND_STROKE
        }
        topLeftRadius = humpCornerRadius
        topRightRadius = humpCornerRadius
        layoutHeight =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64F, resources.displayMetrics)

        humpRy = humpRadius

        when (humpLocation) {
            HUMP_CENTER -> {
                humpRx = mWidth.toFloat() / 2
            }
            HUMP_START -> {
                humpRx = humpRadius
            }
            else -> {
                humpRx = (mWidth - humpRadius)
                humpLocationOffsetX = -humpLocationOffsetX
            }
        }
        arcAngle = humpAngle * PI / 360
        setWillNotDraw(false)
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        //画凸起部分
        makeArcHump()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        mHeight = humpRadius * 2

        setMeasuredDimension(widthSize, mHeight.toInt())
        childViewWidth = (widthSize - humpRadius.toInt() * 2) / itemTextArr.size
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in itemTextArr.indices) {
            val item = itemTextArr[i]
            val icon = itemIconArr[i]
            val tabItem = BottomTabItem(context)
            tabItem.tag = i
            tabItem.setTitle(item)
            tabItem.gravity = Gravity.CENTER
            tabItem.setIcon(icon)
            tabItem.iconType = BottomTabItem.ICON_STATICS
            addView(tabItem)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val childViewTop = (mHeight - layoutHeight + 30).toInt()

        when (humpLocation) {
            HUMP_CENTER -> {
                layoutChildView(humpRadius.toInt() * 2, true)
            }
            HUMP_START -> {
                layoutChildView(humpRadius.toInt() * 2, false)
            }
            else -> {
                layoutChildView(0, false)
            }
        }
    }

    private fun layoutChildView(humpBarrier: Int, isCenter: Boolean) {
        val childViewTop = (mHeight - layoutHeight + 30).toInt()
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is BottomTabItem) {
                if (isCenter) {
                    if (i >= itemTextArr.size / 2) {
                        view.layout(
                            childViewWidth * i + humpBarrier,
                            childViewTop,
                            childViewWidth * (i + 1) + humpBarrier,
                            childViewWidth + childViewTop
                        )
                        continue
                    }
                    view.layout(
                        childViewWidth * i,
                        childViewTop,
                        childViewWidth * (i + 1),
                        childViewWidth + childViewTop
                    )
                } else {
                    view.layout(
                        childViewWidth * i + humpBarrier,
                        childViewTop,
                        childViewWidth * (i + 1) + humpBarrier,
                        childViewWidth + childViewTop
                    )
                }
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val rx = humpRx + humpLocationOffsetX
        with(canvas!!) {
            //底座布局和凸起部分
            //画容器 v2
            layoutPaint.setShadowLayer(
                layoutShadowRadius,
                0f,
                0f,
                getColor(R.color.color_black_333)
            )
            drawPath(makeLayoutPath(), layoutPaint)
            //=======================

            //凸起位置图片部分
            val iconOffset = humpRadius - humpIconSize / 2

            val src = Rect(
                0, 0, humpIconSize.toInt(), humpIconSize.toInt()
            )
            val dst = RectF(
                rx - humpRadius + iconOffset,
                iconOffset,
                rx + humpRadius - iconOffset,
                humpRadius * 2 - iconOffset
            )

            val saveLayer =
                canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null)

            val srcBitmap = makeSrcBitmap()
            val iconBitmap = makeDstIconBitmap()

            drawBitmap(srcBitmap, src, dst, bitmapPaint)

            bitmapPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

            drawBitmap(iconBitmap, src, dst, bitmapPaint)

            bitmapPaint.xfermode = null

            canvas.restoreToCount(saveLayer)

        }

    }

    fun initItemTextArray(): Array<String> = arrayOf("首页", "发现", "广场", "我的")

    fun initItemIconArray(): Array<Int> =
        arrayOf(
            R.drawable.selector_test_add_to_clear,
            R.drawable.ic_left_close_30dp,
            R.drawable.ic_add_30dp,
            R.drawable.ic_bar_chart_30
        )

    fun makeCornerHump() {
        val cornerArr = floatArrayOf(
            topLeftRadius,
            topLeftRadius,
            topRightRadius,
            topRightRadius,
            topLeftRadius,
            topLeftRadius,
            topLeftRadius,
            topLeftRadius
        )
        val rx = humpRx + humpLocationOffsetX
        val hump = RectF(
            rx - humpRadius,
            layoutShadowRadius,
            rx + humpRadius,
            humpRadius * 2
        )
        val humpPath = Path()
        humpPath.addRoundRect(hump, cornerArr, Path.Direction.CW)
        layoutPath.addPath(humpPath)
        requestLayout()
    }

    fun makeArcHump() {
        var layoutOffset = 0.0
        when (humpLocation) {
            HUMP_END -> {
                layoutOffset = humpRadius - abs(sin(arcAngle)) * humpRadius
            }
            HUMP_START -> {
                layoutOffset = -(humpRadius - abs(sin(arcAngle)) * humpRadius)
            }
        }
        //圆弧与下方容器的左侧交点
        val arcLeft = humpRx - humpRadius + layoutOffset
        val arcTop = layoutShadowRadius
        //圆弧与下方容器的右侧交点
        val arcRight = humpRx + humpRadius + layoutOffset
        val arcBottom = humpRadius * 2
        val arcRectF = RectF(arcLeft.toFloat(), arcTop, arcRight.toFloat(), arcBottom)
        layoutPath.addArc(
            arcRectF, -(humpAngle / 2 + 90), humpAngle
        )
        requestLayout()
    }

    /**
     * 计算容器
     */
    private fun makeLayoutPath(): Path {
        //容器顶部逻辑
//        val arcTopOffset = abs(cos(arcAngle)) * humpRadius + layoutShadowRadius - 4
        val layout = RectF(0F, mHeight - layoutHeight, measuredWidth.toFloat(), mHeight)
        layoutPath.addRect(layout, Path.Direction.CW)
        return layoutPath
    }

    private fun makeSrcBitmap(): Bitmap {
        val srcBitmap = Bitmap.createBitmap(
            humpIconSize.toInt(),
            humpIconSize.toInt(),
            Bitmap.Config.ARGB_8888
        )
        val bitmapCanvas = Canvas(srcBitmap)
        bitmapCanvas.drawCircle(
            humpIconSize / 2,
            humpIconSize / 2,
            humpIconSize / 2,
            bitmapPaint
        )
        return srcBitmap
    }

    private fun makeDstIconBitmap(): Bitmap {
        val iconBitmap = Bitmap.createBitmap(
            humpIconSize.toInt(),
            humpIconSize.toInt(),
            Bitmap.Config.ARGB_8888
        )

        val iconOptions = BitmapFactory.Options()
        iconOptions.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, humpIcon, iconOptions)
        iconOptions.inSampleSize = iconOptions.outWidth / iconBitmap.width
        iconOptions.inJustDecodeBounds = false

        val icon = BitmapFactory.decodeResource(resources, humpIcon, iconOptions)
        val iconCanvas = Canvas(iconBitmap)
        val matrix = Matrix()
        matrix.postScale(
            (iconBitmap.width.toFloat() / iconOptions.outWidth),
            (iconBitmap.width.toFloat() / iconOptions.outWidth),
            0f,
            0f
        )
        iconCanvas.drawBitmap(icon, matrix, bitmapPaint)

        return iconBitmap
    }

    private fun getColor(@ColorRes color: Int): Int = context.getColor(color)

}