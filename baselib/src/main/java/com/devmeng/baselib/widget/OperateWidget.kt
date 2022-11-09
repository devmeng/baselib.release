package com.devmeng.baselib.widget

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import com.devmeng.baselib.R

/**
 * Created by Richard -> MHS
 * Date : 2022/6/5  16:35
 * Version : 1
 * Description:需要使用该控件的 Activity 或 Fragment
 * 实现 OnWidgetOperationListener 接口中的方法并对方法参数进行判断及业务逻辑进行编辑
 */
class OperateWidget : AppCompatImageView/*, View.OnClickListener*/ {

    companion object {
        const val WIDGET_BACK = 1
        const val WIDGET_SEARCH = 2
        const val WIDGET_ADD = 3
    }

    private var measureSize = 0
    var onWidgetOperation: OnWidgetOperationListener? = null
    private var operateType = WIDGET_BACK

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typedAttrs =
            context.obtainStyledAttributes(attrs, R.styleable.OperateWidget)
        with(typedAttrs) {
            operateType = getInt(R.styleable.OperateWidget_operateType, operateType)
            recycle()
        }
        initResource(operateType)
//        setOnClickListener(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureSize(widthMeasureSpec)
        measureSize(heightMeasureSpec)

        setMeasuredDimension(measureSize, measureSize)
    }

    private fun measureSize(measureSpec: Int) {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)

        measureSize = if (mode == MeasureSpec.EXACTLY) {
            //match.. or dp exactly
            size
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                60F,
                resources.displayMetrics
            )
                .toInt()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                onWidgetOperation!!.operation(operateType)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun initResource(operateType: Int) {
        var imgRes = 0
        when (operateType) {
            WIDGET_BACK -> {
                imgRes = R.drawable.ic_left_close_30dp
            }
            WIDGET_SEARCH -> {
                imgRes = R.drawable.ic_tb_search_30dp
            }
            WIDGET_ADD -> {
                imgRes = R.drawable.ic_add_30dp
            }
        }
        setImageResource(imgRes)
        requestLayout()
    }

    /*override fun onClick(v: View?) {
        Logger.d("OperateWidget ACTION_UP type -> $operateType")
        onWidgetOperation!!.operation(operateType)
    }*/

    interface OnWidgetOperationListener {
        /**
         * SearchWidget pressed function
         * @param operateType : 根据需求定义控件类型
         */
        fun operation(operateType: Int)

    }

}